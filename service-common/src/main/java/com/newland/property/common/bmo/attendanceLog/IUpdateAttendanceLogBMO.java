package com.newland.property.common.bmo.attendanceLog;
import com.newland.property.po.attendance.AttendanceLogPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttendanceLogBMO {


    /**
     * 修改考勤日志
     * add by wuxw
     * @param attendanceLogPo
     * @return
     */
    ResponseEntity<String> update(AttendanceLogPo attendanceLogPo);


}
