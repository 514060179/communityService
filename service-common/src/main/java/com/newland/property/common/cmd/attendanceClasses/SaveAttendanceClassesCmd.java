/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.attendance.AttendanceClassesDto;
import com.newland.property.intf.common.IAttendanceClassesAttrV1InnerServiceSMO;
import com.newland.property.intf.common.IAttendanceClassesV1InnerServiceSMO;
import com.newland.property.po.attendance.AttendanceClassesPo;
import com.newland.property.po.attendance.AttendanceClassesAttrPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@NewlandPropertyCmdDoc(title = "添加考勤规则",
        description = "外系统添加考勤规则",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/attendanceClasses.saveAttendanceClasses",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "attendanceClasses.saveAttendanceClasses"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "classesName", length = 64, remark = "规则名称"),
        @NewlandPropertyParamDoc(name = "timeOffset", type = "int",length = 30, remark = "打卡范围"),
        @NewlandPropertyParamDoc(name = "lateOffset", type = "int",length = 30, remark = "迟到范围"),
        @NewlandPropertyParamDoc(name = "leaveOffset", type = "int",length = 30, remark = "早退范围"),
        @NewlandPropertyParamDoc(name = "classesObjId",length = 30, remark = "部门ID orgId"),
        @NewlandPropertyParamDoc(name = "classesObjName",length = 64, remark = "部门名称"),

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"classesId\":\"\",\"classesName\":\"测试考勤\",\"timeOffset\":\"10\",\"leaveOffset\":\"10\",\"lateOffset\":\"10\",\"classesObjType\":\"\",\"classesObjId\":\"842022081548770433\"}",
        resBody="{'code':0,'msg':'成功'}"
)

/**
 * 类表述：保存
 * 服务编码：attendanceClasses.saveAttendanceClasses
 * 请求路劲：/app/attendanceClasses.SaveAttendanceClasses
 * add by 吴学文 at 2022-07-16 17:50:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "attendanceClasses.saveAttendanceClasses")
public class SaveAttendanceClassesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveAttendanceClassesCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IAttendanceClassesV1InnerServiceSMO attendanceClassesV1InnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesAttrV1InnerServiceSMO attendanceClassesAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "classesName", "请求报文中未包含classesName");
        Assert.hasKeyAndValue(reqJson, "timeOffset", "请求报文中未包含timeOffset");
        Assert.hasKeyAndValue(reqJson, "lateOffset", "请求报文中未包含lateOffset");
        Assert.hasKeyAndValue(reqJson, "leaveOffset", "请求报文中未包含leaveOffset");


    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesPo.class);
        attendanceClassesPo.setClassesId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        attendanceClassesPo.setStoreId(cmdDataFlowContext.getReqHeaders().get("store-id"));
        attendanceClassesPo.setClockCount("2");
        attendanceClassesPo.setClassesObjType(AttendanceClassesDto.CLASSES_OBJ_TYPE_PARTMENT);
        attendanceClassesPo.setClockType("1001");
        attendanceClassesPo.setClockTypeValue("*");
        //处理房屋属性
        dealAttr(reqJson,attendanceClassesPo, cmdDataFlowContext);

        int flag = attendanceClassesV1InnerServiceSMOImpl.saveAttendanceClasses(attendanceClassesPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void dealAttr(JSONObject reqJson,AttendanceClassesPo attendanceClassesPo, ICmdDataFlowContext context) {

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("classesId", attendanceClassesPo.getClassesId());
            attr.put("storeId", attendanceClassesPo.getStoreId());
            attr.put("attrId",GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            AttendanceClassesAttrPo attendanceClassesAttrPo = BeanConvertUtil.covertBean(attr, AttendanceClassesAttrPo.class);
            flag = attendanceClassesAttrV1InnerServiceSMOImpl.saveAttendanceClassesAttr(attendanceClassesAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

    }
}
