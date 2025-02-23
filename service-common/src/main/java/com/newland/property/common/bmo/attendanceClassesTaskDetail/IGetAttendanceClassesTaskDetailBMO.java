package com.newland.property.common.bmo.attendanceClassesTaskDetail;

import com.newland.property.dto.attendance.AttendanceClassesTaskDto;
import com.newland.property.dto.attendance.AttendanceClassesTaskDetailDto;
import org.springframework.http.ResponseEntity;

public interface IGetAttendanceClassesTaskDetailBMO {


    /**
     * 查询考勤任务明细
     * add by wuxw
     *
     * @param attendanceClassesTaskDetailDto
     * @return
     */
    ResponseEntity<String> get(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);


    /**
     * 查询月考勤
     * add by wuxw
     *
     * @param attendanceClassesTaskDto
     * @return
     */
    ResponseEntity<String> getMonthAttendance(AttendanceClassesTaskDto attendanceClassesTaskDto);
}
