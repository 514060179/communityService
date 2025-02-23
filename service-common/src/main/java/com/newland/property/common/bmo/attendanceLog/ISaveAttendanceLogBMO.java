package com.newland.property.common.bmo.attendanceLog;

import com.newland.property.po.attendance.AttendanceLogPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAttendanceLogBMO {


    /**
     * 添加考勤日志
     * add by wuxw
     * @param attendanceLogPo
     * @return
     */
    ResponseEntity<String> save(AttendanceLogPo attendanceLogPo);


}
