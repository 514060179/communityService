package com.newland.property.community.bmo.roomRenovationDetail.impl;

import com.newland.property.community.bmo.roomRenovationDetail.ISaveRoomRenovationDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRoomRenovationDetailBMOImpl")
public class SaveRoomRenovationDetailBMOImpl implements ISaveRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param roomRenovationDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RoomRenovationDetailPo roomRenovationDetailPo) {

        roomRenovationDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = roomRenovationDetailInnerServiceSMOImpl.saveRoomRenovationDetail(roomRenovationDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
