package com.newland.property.common.bmo.attendanceClassesTaskDetail.impl;

import com.newland.property.common.bmo.attendanceClassesTaskDetail.IDeleteAttendanceClassesTaskDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesTaskDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttendanceClassesTaskDetailBMOImpl")
public class DeleteAttendanceClassesTaskDetailBMOImpl implements IDeleteAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {

        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.deleteAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
