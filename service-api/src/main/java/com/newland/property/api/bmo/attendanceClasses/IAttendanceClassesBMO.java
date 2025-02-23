package com.newland.property.api.bmo.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.IApiBaseBMO;
import com.newland.property.core.context.DataFlowContext;

public interface IAttendanceClassesBMO extends IApiBaseBMO {


    /**
     * 添加考勤班次
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加考勤班次信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除考勤班次
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
