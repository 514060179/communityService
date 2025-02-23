package com.newland.property.common.bmo.attendanceClassesTask.impl;

import com.newland.property.common.bmo.attendanceClassesTask.ISaveAttendanceClassesTaskBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.attendance.AttendanceClassesDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IAttendanceClassesInnerServiceSMO;
import com.newland.property.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.newland.property.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesTaskPo;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveAttendanceClassesTaskBMOImpl")
public class SaveAttendanceClassesTaskBMOImpl implements ISaveAttendanceClassesTaskBMO {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;
    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attendanceClassesTaskPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AttendanceClassesTaskPo attendanceClassesTaskPo,
                                       List<AttendanceClassesTaskDetailPo> attendanceClassesTaskDetailPos) {

        //attendanceClassesTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        //查询班组是否存在
        AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
        attendanceClassesDto.setClassesId(attendanceClassesTaskPo.getClassId());
        List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);

        Assert.listOnlyOne(attendanceClassesDtos, "班组不存在");

        attendanceClassesTaskPo.setStoreId(attendanceClassesDtos.get(0).getStoreId());

        //查询员工信息
        UserDto userDto = new UserDto();
        userDto.setUserId(attendanceClassesTaskPo.getStaffId());
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos,"未包含员工");

        attendanceClassesTaskPo.setStaffName(userDtos.get(0).getName());

        int flag = attendanceClassesTaskInnerServiceSMOImpl.saveAttendanceClassesTask(attendanceClassesTaskPo);

        for (AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo : attendanceClassesTaskDetailPos) {
            attendanceClassesTaskDetailPo.setStoreId(attendanceClassesDtos.get(0).getStoreId());
            attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);
        }

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
