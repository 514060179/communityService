package com.newland.property.common.bmo.attendanceClassesTaskDetail;

import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAttendanceClassesTaskDetailBMO {


    /**
     * 添加考勤任务明细
     * add by wuxw
     * @param attendanceClassesTaskDetailPo
     * @return
     */
    ResponseEntity<String> save(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo);


}
