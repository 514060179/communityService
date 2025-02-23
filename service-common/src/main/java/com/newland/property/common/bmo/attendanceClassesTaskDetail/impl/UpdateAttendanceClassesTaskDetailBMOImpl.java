package com.newland.property.common.bmo.attendanceClassesTaskDetail.impl;

import com.newland.property.common.bmo.attendanceClassesTaskDetail.IUpdateAttendanceClassesTaskDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.newland.property.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttendanceClassesTaskDetailBMOImpl")
public class UpdateAttendanceClassesTaskDetailBMOImpl implements IUpdateAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;


    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo, boolean finishAllTaskDetail) {

        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.updateAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        if (finishAllTaskDetail) {
            AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
            attendanceClassesTaskPo.setTaskId(attendanceClassesTaskDetailPo.getTaskId());
            attendanceClassesTaskPo.setState("30000");
            attendanceClassesTaskPo.setStatusCd("0");
            attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);
        }else{
            AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
            attendanceClassesTaskPo.setTaskId(attendanceClassesTaskDetailPo.getTaskId());
            attendanceClassesTaskPo.setState("20000");
            attendanceClassesTaskPo.setStatusCd("0");
            attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
