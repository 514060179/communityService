package com.newland.property.common.bmo.attendanceLog.impl;

import com.newland.property.common.bmo.attendanceLog.IUpdateAttendanceLogBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttendanceLogInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttendanceLogBMOImpl")
public class UpdateAttendanceLogBMOImpl implements IUpdateAttendanceLogBMO {

    @Autowired
    private IAttendanceLogInnerServiceSMO attendanceLogInnerServiceSMOImpl;

    /**
     * @param attendanceLogPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AttendanceLogPo attendanceLogPo) {

        int flag = attendanceLogInnerServiceSMOImpl.updateAttendanceLog(attendanceLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
