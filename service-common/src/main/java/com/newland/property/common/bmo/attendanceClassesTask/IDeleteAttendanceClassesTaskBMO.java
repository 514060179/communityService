package com.newland.property.common.bmo.attendanceClassesTask;
import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAttendanceClassesTaskBMO {


    /**
     * 修改考勤任务
     * add by wuxw
     * @param attendanceClassesTaskPo
     * @return
     */
    ResponseEntity<String> delete(AttendanceClassesTaskPo attendanceClassesTaskPo);


}
