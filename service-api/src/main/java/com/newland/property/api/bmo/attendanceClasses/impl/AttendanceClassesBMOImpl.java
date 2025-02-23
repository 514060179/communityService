package com.newland.property.api.bmo.attendanceClasses.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.attendanceClasses.IAttendanceClassesBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IAttendanceClassesInnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("attendanceClassesBMOImpl")
public class AttendanceClassesBMOImpl extends ApiBaseBMO implements IAttendanceClassesBMO {

    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("classesId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_classesId));
        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.insert(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.update(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ATTENDANCE_CLASSES);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.update(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ATTENDANCE_CLASSES);
    }

}
