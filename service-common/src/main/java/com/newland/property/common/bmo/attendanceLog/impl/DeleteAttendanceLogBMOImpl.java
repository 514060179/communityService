package com.newland.property.common.bmo.attendanceLog.impl;

import com.newland.property.common.bmo.attendanceLog.IDeleteAttendanceLogBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttendanceLogInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttendanceLogBMOImpl")
public class DeleteAttendanceLogBMOImpl implements IDeleteAttendanceLogBMO {

    @Autowired
    private IAttendanceLogInnerServiceSMO attendanceLogInnerServiceSMOImpl;

    /**
     * @param attendanceLogPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AttendanceLogPo attendanceLogPo) {

        int flag = attendanceLogInnerServiceSMOImpl.deleteAttendanceLog(attendanceLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
