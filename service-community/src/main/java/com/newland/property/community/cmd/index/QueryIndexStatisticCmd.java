package com.newland.property.community.cmd.index;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.ApiIndexStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "index.queryIndexStatistic")
public class QueryIndexStatisticCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setPage(1);
        storeDto.setRow(1);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");
// 查询业主 总数量
        ApiIndexStatisticVo apiIndexStatisticVo = new ApiIndexStatisticVo();
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        int ownerCount = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        int noEnterRoomOwnerCount = ownerInnerServiceSMOImpl.queryNoEnterRoomOwnerCount(ownerDto);
        // 查询房屋 总数量
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        int roomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        int freeRoomCount = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(roomDto);
        // 查询停车位 总数量
        int parkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class));
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        parkingSpaceDto.setState("F");
        int freeParkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        // 查询商铺 总数量
        roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setRoomType(RoomDto.ROOM_TYPE_SHOPS);
        int shopCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        int freeShopCount = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(roomDto);



        apiIndexStatisticVo.setOwnerCount(ownerCount + "");
        apiIndexStatisticVo.setNoEnterRoomCount(noEnterRoomOwnerCount + "");
        apiIndexStatisticVo.setRoomCount(roomCount + "");
        apiIndexStatisticVo.setFreeRoomCount(freeRoomCount + "");
        apiIndexStatisticVo.setParkingSpaceCount(parkingSpaceCount + "");
        apiIndexStatisticVo.setFreeParkingSpaceCount(freeParkingSpaceCount + "");
        apiIndexStatisticVo.setShopCount(shopCount + "");
        apiIndexStatisticVo.setFreeShopCount(freeShopCount + "");
        apiIndexStatisticVo.setStoreTypeCd(storeDtos.get(0).getStoreTypeCd());
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiIndexStatisticVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
