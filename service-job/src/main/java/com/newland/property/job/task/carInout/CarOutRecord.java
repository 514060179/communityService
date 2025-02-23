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

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author fengtianying
 * @description 车辆出入场记录
 * @date 2024-06-02 14:30
 */
@Component
public class CarOutRecord extends TaskSystemQuartz {

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
        logger.debug("开始执行同步出入场记录" + taskDto.toString());
        String url = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.CAR_OUT_URL).getValue();
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
            //获取同步时间
            String lastTime = null;
            Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.CAR_OUT_SYNC_TIME);
            String paIdTime = CommonCache.getValue(DooRConstant.REDIS_PREFIX_SYNC_CAR_OUT_TIME + parkingAreaDto.getCommunityId() + ":" + paId);
            while (pages == null || page <= pages) {
                String token = DoorUtil.getToken(split);
                if (token == null) {
                    logger.warn("车场" + parkingAreaDto.getPaId() + "未配置出云token");
                    continue;
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("token", token);
                headers.add("Referer", "139.9.36.54");
                headers.add("Content-Type", "application/json");
                // 获取入场数据，并同步到库中
                CarInoutRequest request = new CarInoutRequest();

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
                request.setPageNum(page);
                HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(request), headers);
                logger.debug("获取token request param={},request header={}", request, headers);
                ResponseEntity<String> newExchange = outRestTemplate.newExchange(url, HttpMethod.POST, httpEntity, String.class);
                String body = newExchange.getBody();
                logger.debug("获取token response={}", body);
                CarInoutResponse carInoutResponse = JSON.parseObject(body, CarInoutResponse.class);
                if ("1".equals(carInoutResponse.getHead().getStatus())) {
                    //组装数据插入库中
                    JSONObject carInoutDtos = carInoutResponse.getBody();
                    JSONArray list = carInoutDtos.getJSONArray("list");
                    if (list == null) {
                        logger.warn("车场" + parkingAreaDto.getPaId() + "云同步出入场数据为空");
                        pages = 0;
                        continue;
                    }
                    list.forEach(item -> {
                        carOutRecord(parkingAreaDto, (JSONObject) item);
                    });
                    pages = carInoutDtos.getInteger("pages");
                    page++;
                } else {
                    pages = 0;
                    logger.error("车场" + parkingAreaDtos.get(i).getPaId() + "出云同步失败");
                }

            }
            if (pages != 0) {
                CommonCache.setValue(DooRConstant.REDIS_PREFIX_SYNC_CAR_OUT_TIME + parkingAreaDto.getCommunityId() + ":" + paId, lastTime);
                mapping.setValue(lastTime);
                MappingCache.setVaule(mapping);
            }
        }
        logger.debug("结束执行同步出入场记录" + taskDto.toString());
    }

    private void carOutRecord(ParkingAreaDto parkingAreaDto, JSONObject tmp) {
        //更新car_inout、car_inout_detail表
        tmp.getString("carNo");
        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setCommunityId(parkingAreaDto.getCommunityId());
        carInoutPo.setCarNum(tmp.getString("carNo"));
        carInoutPo.setState(CarInoutDto.STATE_OUT);
        carInoutPo.setInTime(tmp.getString("inTime"));

        String carNo = tmp.getString("carNo");
        String inTime = tmp.getString("inTime");
        String inoutId = null;
        try {
            Date inTimeDate = DateUtil.getDateFromStringAdaptTwoPattern(inTime);
            int abs = Math.abs(carNo.hashCode());
            String formatTimeString = DateUtil.getFormatTimeString(inTimeDate, DateUtil.DATE_FORMATE_STRING_DEFAULT);
            inoutId = abs + formatTimeString;
        } catch (ParseException e) {
            logger.error("车场" + parkingAreaDto.getPaId() + "出云同步失败");
            e.printStackTrace();
        }
        carInoutPo.setInoutId(inoutId);
        carInoutPo.setOutTime(tmp.getString("outTime"));
        carInoutPo.setStatusCd(parkingAreaDto.getStatusCd());
        carInoutPo.setPaId(parkingAreaDto.getPaId());
        CarInoutDetailPo carInoutDetailPo = new CarInoutDetailPo();
        carInoutDetailPo.setInoutId(inoutId);
        carInoutDetailPo.setCommunityId(parkingAreaDto.getCommunityId());
        carInoutDetailPo.setMachineId(tmp.getString("appearancesId"));//出场控制器编号
        carInoutDetailPo.setMachineCode(tmp.getString("appearancesId"));
        carInoutDetailPo.setCarInout(CarInoutDetailDto.CAR_INOUT_OUT);
        carInoutDetailPo.setCarNum(tmp.getString("carNo"));
        carInoutDetailPo.setStatusCd(parkingAreaDto.getStatusCd());
        carInoutDetailPo.setPaId(parkingAreaDto.getPaId());
        carInoutDetailPo.setRemark(tmp.getString("outWayName"));
        carInoutDetailPo.setState(CarInoutDto.STATE_OUT);
        carInoutDetailPo.setCarType(OwnerCarDto.LEASE_TYPE_MONTH);
        carInoutDetailPo.setCarTypeName("月租车");
        Mapping imgMapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.INOUT_IMG_URL);
        String imgUrl = imgMapping.getValue();
        carInoutDetailPo.setPhotoJpg(imgUrl + tmp.getString("outPic"));
        saveOrUpdateCarInout(carInoutPo);
        saveOrUpdateCarInoutDetail(carInoutDetailPo, imgUrl + tmp.getString("inPic"));
    }

    private void saveOrUpdateCarInoutDetail(CarInoutDetailPo carInoutDetailPo, String imgUrl) {
        String carOut = CarInoutDetailDto.CAR_INOUT_OUT;
        String carIn = CarInoutDetailDto.CAR_INOUT_IN;
        String photoJpg = carInoutDetailPo.getPhotoJpg();
        carInoutDetailPo.setPhotoJpg(imgUrl);
        carInoutDetailPo.setInoutType(1);
        carInoutDetailPo.setState(CarInoutDetailDto.STATE_OUT);
        //更新入场记录
        carInoutDetailPo.setCarInout(carIn);
        if (iCarInoutDetailV1InnerServiceSMO.updateCarInoutDetail(carInoutDetailPo) <= 0) {
            carInoutDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            iCarInoutDetailV1InnerServiceSMO.saveCarInoutDetail(carInoutDetailPo);
        }
        //更新出场记录
        carInoutDetailPo.setDetailId(null);
        carInoutDetailPo.setCarInout(carOut);
        carInoutDetailPo.setPhotoJpg(photoJpg);
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
