package com.newland.property.report.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.report.IReportCommunityInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询房屋变更记录
 */
@NewlandPropertyCmd(serviceCode = "room.queryHisRoom")
public class QueryHisRoomCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        int row = reqJson.getInteger("row");
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        String roomName = reqJson.getString("roomName");
        if(!StringUtil.isEmpty(roomName)){
            String[] roomNames = roomName.split("-");
            if(roomNames.length == 3) {
                roomDto.setFloorNum(roomNames[0]);
                roomDto.setUnitNum(roomNames[1]);
                roomDto.setRoomNum(roomNames[2]);
            }
        }

        int total = reportCommunityInnerServiceSMOImpl.queryHisRoomCount(roomDto);
//        int count = 0;
        List<RoomDto> roomDtos = null;
        if (total > 0) {
            roomDtos = reportCommunityInnerServiceSMOImpl.queryHisRooms(roomDto);
        } else {
            roomDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, roomDtos);
        context.setResponseEntity(responseEntity);
    }
}
