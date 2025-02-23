package com.newland.property.community.bmo.roomRenovationRecord.impl;

import com.newland.property.community.bmo.roomRenovationRecord.IDeleteRoomRenovationRecordBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationRecordPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 删除装修记录
 *
 * @author fqz
 * @date 2021-02-27 11:52
 */
@Service("deleteRoomRenovationRecordBMOImpl")
public class DeleteRoomRenovationRecordBMOImpl implements IDeleteRoomRenovationRecordBMO {

    @Autowired
    private IRoomRenovationRecordInnerServiceSMO roomRenovationRecordInnerServiceSMOImpl;

    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RoomRenovationRecordPo roomRenovationRecordPo) {
        int flag = roomRenovationRecordInnerServiceSMOImpl.deleteRoomRenovationRecord(roomRenovationRecordPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
