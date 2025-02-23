package com.newland.property.common.bmo.attendanceClassesTaskDetail.impl;

import com.newland.property.common.bmo.attendanceClassesTaskDetail.IGetAttendanceClassesTaskDetailBMO;
import com.newland.property.dto.attendance.AttendanceClassesTaskDto;
import com.newland.property.dto.attendance.AttendanceClassesTaskDetailDto;
import com.newland.property.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.newland.property.intf.report.IReportAttendanceInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttendanceClassesTaskDetailBMOImpl")
public class GetAttendanceClassesTaskDetailBMOImpl implements IGetAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IReportAttendanceInnerServiceSMO reportAttendanceInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDetailDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto) {


        int count = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetailsCount(attendanceClassesTaskDetailDto);

        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetailDtos = null;
        if (count > 0) {
            attendanceClassesTaskDetailDtos = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetails(attendanceClassesTaskDetailDto);
        } else {
            attendanceClassesTaskDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDetailDto.getRow()), count, attendanceClassesTaskDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> getMonthAttendance(AttendanceClassesTaskDto attendanceClassesTaskDto) {
        int count = reportAttendanceInnerServiceSMOImpl.getMonthAttendanceCount(attendanceClassesTaskDto);

        List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = null;
        if (count > 0) {
            attendanceClassesTaskDtos = reportAttendanceInnerServiceSMOImpl.getMonthAttendance(attendanceClassesTaskDto);
        } else {
            attendanceClassesTaskDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDto.getRow()), count, attendanceClassesTaskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
