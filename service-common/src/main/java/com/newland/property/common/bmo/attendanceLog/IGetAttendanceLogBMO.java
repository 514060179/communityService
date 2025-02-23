package com.newland.property.common.bmo.attendanceLog;
import com.newland.property.dto.attendance.AttendanceLogDto;
import org.springframework.http.ResponseEntity;
public interface IGetAttendanceLogBMO {


    /**
     * 查询考勤日志
     * add by wuxw
     * @param  attendanceLogDto
     * @return
     */
    ResponseEntity<String> get(AttendanceLogDto attendanceLogDto);


}
