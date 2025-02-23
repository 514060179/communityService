package com.newland.property.common.bmo.attendanceClassesTaskDetail.impl;

import com.newland.property.common.bmo.attendanceClassesTaskDetail.ISaveAttendanceClassesTaskDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAttendanceClassesTaskDetailBMOImpl")
public class SaveAttendanceClassesTaskDetailBMOImpl implements ISaveAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attendanceClassesTaskDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {

        attendanceClassesTaskDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
