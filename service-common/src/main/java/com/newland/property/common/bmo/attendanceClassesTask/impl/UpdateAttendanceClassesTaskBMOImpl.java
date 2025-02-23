package com.newland.property.common.bmo.attendanceClassesTask.impl;

import com.newland.property.common.bmo.attendanceClassesTask.IUpdateAttendanceClassesTaskBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttendanceClassesTaskBMOImpl")
public class UpdateAttendanceClassesTaskBMOImpl implements IUpdateAttendanceClassesTaskBMO {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AttendanceClassesTaskPo attendanceClassesTaskPo) {

        int flag = attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
