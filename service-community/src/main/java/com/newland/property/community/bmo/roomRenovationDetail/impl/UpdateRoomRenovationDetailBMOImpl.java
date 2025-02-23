package com.newland.property.community.bmo.roomRenovationDetail.impl;

import com.newland.property.community.bmo.roomRenovationDetail.IUpdateRoomRenovationDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRoomRenovationDetailBMOImpl")
public class UpdateRoomRenovationDetailBMOImpl implements IUpdateRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * @param roomRenovationDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RoomRenovationDetailPo roomRenovationDetailPo) {

        int flag = roomRenovationDetailInnerServiceSMOImpl.updateRoomRenovationDetail(roomRenovationDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
