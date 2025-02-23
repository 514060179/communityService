package com.newland.property.community.bmo.roomRenovation.impl;

import com.newland.property.community.bmo.roomRenovation.IUpdateRoomRenovationBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.community.IRoomRenovationInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRoomRenovationBMOImpl")
public class UpdateRoomRenovationBMOImpl implements IUpdateRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * @param roomRenovationPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RoomRenovationPo roomRenovationPo) {

        int flag = roomRenovationInnerServiceSMOImpl.updateRoomRenovation(roomRenovationPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

    /**
     * 修改房屋状态
     *
     * @param roomDto
     * @return
     */
    @Override
    public ResponseEntity<String> updateRoom(RoomDto roomDto) {
        int flag = roomRenovationInnerServiceSMOImpl.updateRoom(roomDto);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
