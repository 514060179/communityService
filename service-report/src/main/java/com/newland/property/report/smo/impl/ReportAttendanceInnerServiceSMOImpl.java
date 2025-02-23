package com.newland.property.report.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.attendance.AttendanceClassesTaskDetailDto;
import com.newland.property.dto.attendance.AttendanceClassesTaskDto;
import com.newland.property.intf.report.IReportAttendanceInnerServiceSMO;
import com.newland.property.report.dao.IReportAttendanceServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用月统计内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportAttendanceInnerServiceSMOImpl extends BaseServiceSMO implements IReportAttendanceInnerServiceSMO {

    @Autowired
    private IReportAttendanceServiceDao reportAttendanceServiceDaoImpl;


    @Override
    public int getMonthAttendanceCount(@RequestBody  AttendanceClassesTaskDto attendanceClassesTaskDto) {
        return reportAttendanceServiceDaoImpl.getMonthAttendanceCount(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDto));
    }

    @Override
    public List<AttendanceClassesTaskDto> getMonthAttendance(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto) {
        int page = attendanceClassesTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceClassesTaskDto.setPage((page - 1) * attendanceClassesTaskDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(reportAttendanceServiceDaoImpl.getMonthAttendance(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDto)),AttendanceClassesTaskDto.class);
    }

    @Override
    public List<AttendanceClassesTaskDetailDto> getMonthAttendanceDetail(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto) {
        return BeanConvertUtil.covertBeanList(reportAttendanceServiceDaoImpl.getMonthAttendanceDetail(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDto)),AttendanceClassesTaskDetailDto.class);
    }
}
