package com.newland.property.common.cmd.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.dto.advert.AdvertDto;
import com.newland.property.dto.advert.AdvertItemDto;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.intf.common.IAdvertInnerServiceSMO;
import com.newland.property.intf.common.IAdvertItemInnerServiceSMO;
import com.newland.property.intf.common.IMachineInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.community.IUnitInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "advert.listAdvertPhotoAndVedios")
public class ListAdvertPhotoAndVediosCmd extends Cmd {

    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备编码");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));

        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "根据设备编码查询到多条设备信息或一条都没有");

        String locationTypeCd = machineDtos.get(0).getLocationTypeCd();
        String locationObjId = machineDtos.get(0).getLocationObjId();

        JSONArray advertPhotoAndVideos = new JSONArray();

        /**
         *
         * 1000">东大门
         * 1001">西大门
         * 1002">北大门
         * 1003">南大门
         * 2000">单元门
         * 3000">房屋门
         */

        //如果是大门 则只获取小区的广告
        if (!"2000".equals(locationTypeCd) && !"3000".equals(locationTypeCd)) {
            getCommunityAdvert(reqJson.getString("communityId"), advertPhotoAndVideos);
        } else if ("2000".equals(locationTypeCd)) {//单元门口设备
            //查询 单元的广告，没有找 楼栋，再找找不到 就要查小区
            if (getUnitAdvert(reqJson.getString("communityId"), locationObjId, advertPhotoAndVideos)) {
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }

            // 查询楼栋广告
            if (getFloorAdvert(reqJson.getString("communityId"), locationObjId, advertPhotoAndVideos)) {
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }

            if (getCommunityAdvert(reqJson.getString("communityId"), advertPhotoAndVideos)) {
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }
            ;

        } else if ("3000".equals(locationTypeCd)) {
            //先查询房屋广告信息
            AdvertDto advertDto = new AdvertDto();
            advertDto.setCommunityId(reqJson.getString("communityId"));
            advertDto.setLocationObjId(locationObjId);
            List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);
            if (advertDtos != null && advertDtos.size() != 0) {
                this.getAdvertItem(advertDtos, advertPhotoAndVideos);
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }
            //房屋找不到，找 单元
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(locationObjId);
            roomDto.setCommunityId(reqJson.getString("communityId"));
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "未找到房屋或查询多条房屋信息");
            if (getUnitAdvert(reqJson.getString("communityId"), roomDtos.get(0).getUnitId(), advertPhotoAndVideos)) {
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }

            // 查询楼栋广告
            if (getFloorAdvert(reqJson.getString("communityId"), roomDtos.get(0).getUnitId(), advertPhotoAndVideos)) {
                responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }

            // 查询小区广告
            getCommunityAdvert(reqJson.getString("communityId"), advertPhotoAndVideos);
        }

        responseEntity = new ResponseEntity<String>(advertPhotoAndVideos.toJSONString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);

    }

    private boolean getCommunityAdvert(String communityId, JSONArray advertPhotoAndVideos) {
        AdvertDto advertDto = new AdvertDto();
        advertDto.setCommunityId(communityId);
        advertDto.setLocationObjId(communityId);
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhotoAndVideos);
            return true;
        }

        return false;
    }

    /**
     * 查询楼栋 广告
     *
     * @param communityId
     * @param unitId
     * @param advertPhotoAndVideos
     * @return
     */
    private boolean getFloorAdvert(String communityId, String unitId, JSONArray advertPhotoAndVideos) {
        UnitDto unitDto = new UnitDto();
        unitDto.setUnitId(unitId);
        unitDto.setCommunityId(communityId);

        List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);

        Assert.listOnlyOne(unitDtos, "根据单元查到多条数据或未查询到数据");

        AdvertDto advertDto = new AdvertDto();
        advertDto.setCommunityId(communityId);
        advertDto.setLocationObjId(unitDtos.get(0).getFloorId());
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);
        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhotoAndVideos);
            return true;
        }
        return false;
    }

    private boolean getUnitAdvert(String communityId, String unitId, JSONArray advertPhotoAndVideos) {
        AdvertDto advertDto = new AdvertDto();
        advertDto.setCommunityId(communityId);
        advertDto.setLocationObjId(unitId);
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhotoAndVideos);
            return true;
        }

        return false;
    }

    /**
     * 查询广告照片
     *
     * @param advertDtos
     * @param advertPhotoAndVideos
     */
    private void getAdvertItem(List<AdvertDto> advertDtos, JSONArray advertPhotoAndVideos) {
        JSONObject photoAndVideo = null;

        for (AdvertDto advertDto : advertDtos) {

            AdvertItemDto advertItemDto = new AdvertItemDto();
            advertItemDto.setAdvertId(advertDto.getAdvertId());
            advertItemDto.setCommunityId(advertDto.getCommunityId());
            List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);

            for (AdvertItemDto tmpAdvertItemDto : advertItemDtos) {

                //照片
                if ("8888".equals(tmpAdvertItemDto.getItemTypeCd())) {
                    photoAndVideo = new JSONObject();
                    photoAndVideo.put("suffix", "JPEG");
                    photoAndVideo.put("url", "/callComponent/download/getFile/file?fileId=" + tmpAdvertItemDto.getUrl() + "&communityId=" + advertDto.getCommunityId());
                    photoAndVideo.put("seq", tmpAdvertItemDto.getSeq());
                    advertPhotoAndVideos.add(photoAndVideo);
                } else if ("9999".equals(tmpAdvertItemDto.getItemTypeCd())) {
                    photoAndVideo = new JSONObject();
                    photoAndVideo.put("suffix", "VIDEO");
                    photoAndVideo.put("url", "/video/" + tmpAdvertItemDto.getUrl());
                    photoAndVideo.put("seq", tmpAdvertItemDto.getSeq());
                    advertPhotoAndVideos.add(photoAndVideo);

                }

            }
        }

    }
}
