package com.newland.property.common.bmo.attendanceClassesTask;

import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaveAttendanceClassesTaskBMO {


    /**
     * 添加考勤任务
     * add by wuxw
     * @param attendanceClassesTaskPo
     * @return
     */
    ResponseEntity<String> save(AttendanceClassesTaskPo attendanceClassesTaskPo,
                                List<AttendanceClassesTaskDetailPo> attendanceClassesTaskDetailPos);


}
