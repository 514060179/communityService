package com.newland.property.common.bmo.attendanceClassesTask;

import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttendanceClassesTaskBMO {


    /**
     * 修改考勤任务
     * add by wuxw
     *
     * @param attendanceClassesTaskPo
     * @return
     */
    ResponseEntity<String> update(AttendanceClassesTaskPo attendanceClassesTaskPo);


}
