package com.newland.property.common.bmo.attendanceClassesTaskDetail;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAttendanceClassesTaskDetailBMO {


    /**
     * 修改考勤任务明细
     * add by wuxw
     * @param attendanceClassesTaskDetailPo
     * @return
     */
    ResponseEntity<String> delete(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo);


}
