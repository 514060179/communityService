package com.newland.property.community.bmo.roomRenovation.impl;

import com.newland.property.community.bmo.roomRenovation.ISaveRoomRenovationBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.IRoomRenovationInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRoomRenovationBMOImpl")
public class SaveRoomRenovationBMOImpl implements ISaveRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param roomRenovationPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RoomRenovationPo roomRenovationPo) {

        roomRenovationPo.setrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rId));
        int flag = roomRenovationInnerServiceSMOImpl.saveRoomRenovation(roomRenovationPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
