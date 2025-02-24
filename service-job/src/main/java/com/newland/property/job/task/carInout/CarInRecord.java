package com.newland.property.job.task.carInout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.machine.CarInoutDetailDto;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.mapping.Mapping;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingAreaAttrDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.common.ICarInoutDetailV1InnerServiceSMO;
import com.newland.property.intf.common.ICarInoutV1InnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaV1InnerServiceSMO;
import com.newland.property.job.dto.carinout.CarInoutRequest;
import com.newland.property.job.dto.carinout.CarInoutResponse;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.job.util.DoorUtil;
import com.newland.property.po.car.CarInoutDetailPo;
import com.newland.property.po.car.CarInoutPo;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.DooRConstant;
import com.newland.property.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.newland.property.utils.cache.CommonCache.TOKEN_EXPIRE_TIME;

/**
 * @author fengtianying
 * @description 车辆在场记录
 * @date 2024-06-02 14:30
 */
@Component
public class CarInRecord extends TaskSystemQuartz {

    @Autowired
    private ICarInoutV1InnerServiceSMO iCarInoutV1InnerServiceSMO;
    @Autowired
    private ICarInoutDetailV1InnerServiceSMO iCarInoutDetailV1InnerServiceSMO;
    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;
    @Autowired
    private OutRestTemplate outRestTemplate;

    public static final String CODE_PREFIX_ID = "10";


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void process(TaskDto taskDto) throws ParseException {
        logger.debug("开始执行同步在场记录" + taskDto.toString());
        String url = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.CAR_IN_URL).getValue();
        // 获取所有车场信息
        ParkingAreaDto findParkingAreaDto = new ParkingAreaDto();
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(findParkingAreaDto);
        for (int i = 0; i < parkingAreaDtos.size(); i++) {
            String outAppid = null;
            ParkingAreaDto parkingAreaDto = parkingAreaDtos.get(i);
            List<ParkingAreaAttrDto> attrs = parkingAreaDto.getAttrs();
            if (attrs != null && attrs.size() > 0) {
                for (int j = 0; j < attrs.size(); j++) {
                    ParkingAreaAttrDto parkingAreaAttrDto = attrs.get(j);
                    if ("6185-17861".equals(parkingAreaAttrDto.getSpecCd()) && parkingAreaAttrDto.getValue() != null && parkingAreaAttrDto.getValue().contains("-")) {
                        outAppid = parkingAreaAttrDto.getValue();
                        continue;
                    }
                }
            }
            if (outAppid == null) {
                logger.warn("车场" + parkingAreaDto.getPaId() + "未配置出云appid");
                continue;
            }
            String[] split = outAppid.split("-");
            String paId = split[0];
            int page = 1;
            Integer pages = null;
            String lastTime = null;
            //获取同步时间
            Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.CAR_IN_SYNC_TIME);
            String paIdTime = CommonCache.getValue(DooRConstant.REDIS_PREFIX_SYNC_CAR_IN_TIME + parkingAreaDto.getCommunityId() + ":" + paId);
            while (pages == null || page <= pages) { //2298
                String token = null;
                try {
                    token = DoorUtil.getToken(split);
                } catch (Exception e) {
                    logger.error("车场" + parkingAreaDto.getPaId() + "获取token失败");
                    e.printStackTrace();
                }
                if (token == null) {
                    logger.warn("车场" + parkingAreaDto.getPaId() + "未配置出云token");
                    pages = 0;
                    continue;
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("token", token);
                headers.add("Referer", "139.9.36.54");
                headers.add("Content-Type", "application/json");
                // 获取入场数据，并同步到库中
                CarInoutRequest request = new CarInoutRequest();
                request.setPageNum(page);

                String value = mapping.getValue();
                if (paIdTime == null) {
                    request.setStartTime(value);
                    lastTime = DateUtil.getFormatTimeStringA(new Date());
                    request.setEndTime(lastTime);
                } else {
                    request.setStartTime(paIdTime);
                    lastTime = DateUtil.getFormatTimeStringA(new Date());
                    request.setEndTime(lastTime);
                }
                request.setParkId(paId);
                HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(request), headers);
                ResponseEntity<String> newExchange = outRestTemplate.newExchange(url, HttpMethod.POST, httpEntity, String.class);
                String body = newExchange.getBody();
                CarInoutResponse carInoutResponse = JSON.parseObject(body, CarInoutResponse.class);
                if ("1".equals(carInoutResponse.getHead().getStatus())) {
                    //组装数据插入库中
                    JSONObject carInoutDtos = carInoutResponse.getBody();
                    JSONArray list = carInoutDtos.getJSONArray("list");
                    if (list == null) {
                        logger.warn("车场" + parkingAreaDto.getPaId() + "云同步在场数据为空");
                        pages = 0;
                        continue;
                    }
                    list.forEach(item -> {
                        carInRecord(parkingAreaDto, paId, (JSONObject) item);
                    });
                    pages = carInoutDtos.getInteger("pages");
                    page++;
                } else {
                    logger.error("车场" + parkingAreaDtos.get(i).getPaId() + "出云同步失败");
                    pages = 0;
                }
            }
            if (pages != 0) {
                CommonCache.setValue(DooRConstant.REDIS_PREFIX_SYNC_CAR_IN_TIME + parkingAreaDto.getCommunityId() + ":" + paId, lastTime);
                mapping.setValue(lastTime);
                MappingCache.setVaule(mapping);
            }
        }
        logger.debug("结束执行同步在场记录" + taskDto.toString());
    }

    private void carInRecord(ParkingAreaDto parkingAreaDto, String paId, JSONObject tmp) {
        String inoutId = null;
        String carNo = tmp.getString("carNo");
        String inTime = tmp.getString("inTime");
        try {
            Date inTimeDate = DateUtil.getDateFromStringAdaptTwoPattern(inTime);
            int abs = Math.abs(carNo.hashCode());
            String formatTimeString = DateUtil.getFormatTimeString(inTimeDate, DateUtil.DATE_FORMATE_STRING_DEFAULT);
            inoutId = abs + formatTimeString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setInoutId(inoutId);
        carInoutPo.setCommunityId(parkingAreaDto.getCommunityId());
        carInoutPo.setCarNum(carNo);
        carInoutPo.setState(CarInoutDto.STATE_IN);
        carInoutPo.setInTime(inTime);
//        carInoutPo.setOutTime("");
        carInoutPo.setStatusCd(parkingAreaDto.getStatusCd());
        carInoutPo.setPaId(parkingAreaDto.getPaId());
        CarInoutDetailPo carInoutDetailPo = new CarInoutDetailPo();
        carInoutDetailPo.setInoutId(inoutId);
        carInoutDetailPo.setCommunityId(parkingAreaDto.getCommunityId());
        carInoutDetailPo.setMachineId(tmp.getString("entranceId"));//岗亭ID
        carInoutDetailPo.setMachineCode(tmp.getString("entranceId"));
        carInoutDetailPo.setCarInout(CarInoutDetailDto.CAR_INOUT_IN);
        carInoutDetailPo.setCarNum(tmp.getString("carNo"));
        carInoutDetailPo.setStatusCd(parkingAreaDto.getStatusCd());
        carInoutDetailPo.setPaId(parkingAreaDto.getPaId());
        carInoutDetailPo.setRemark((tmp.getString("correctCarNo") != null && !"".equals(tmp.getString("correctCarNo"))) ? "矫正车牌：" + tmp.getString("correctCarNo") : "");
        carInoutDetailPo.setState(CarInoutDto.STATE_IN);
        carInoutDetailPo.setCarType(OwnerCarDto.LEASE_TYPE_MONTH);
        carInoutDetailPo.setCarTypeName("月租车");
        Mapping imgMapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.INOUT_IMG_URL);
        String imgUrl = imgMapping.getValue();
        carInoutDetailPo.setPhotoJpg(imgUrl + tmp.getString("inPic"));
        saveOrUpdateCarInout(carInoutPo);
        saveOrUpdateCarInoutDetail(carInoutDetailPo);
    }

    private void saveOrUpdateCarInoutDetail(CarInoutDetailPo carInoutDetailPo) {
        if (iCarInoutDetailV1InnerServiceSMO.updateCarInoutDetail(carInoutDetailPo) <= 0) {
            carInoutDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            iCarInoutDetailV1InnerServiceSMO.saveCarInoutDetail(carInoutDetailPo);
        }
    }

    private void saveOrUpdateCarInout(CarInoutPo carInoutPo) {
        if (iCarInoutV1InnerServiceSMO.updateCarInout(carInoutPo) <= 0) {
            iCarInoutV1InnerServiceSMO.saveCarInout(carInoutPo);
        }
    }
}
