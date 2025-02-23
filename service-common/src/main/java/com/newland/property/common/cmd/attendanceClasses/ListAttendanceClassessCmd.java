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

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.common.IAttendanceClassesInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.newland.property.dto.attendance.AttendanceClassesDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@NewlandPropertyCmdDoc(title = "查询考勤规则",
        description = "外系统查询考勤规则",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/attendanceClasses.listAttendanceClassess",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "attendanceClasses.listAttendanceClassess"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "classesId", length = 30, remark = "班级ID"),
        @NewlandPropertyParamDoc(name = "page",type = "int",length = 11, remark = "分页页数"),
        @NewlandPropertyParamDoc(name = "row",type = "int", length = 11, remark = "分页行数"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "classesName", length = 64, remark = "规则名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "timeOffset", type = "int",length = 11, remark = "打卡范围"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "lateOffset", type = "int",length = 11, remark = "迟到范围"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "leaveOffset", type = "int",length = 11, remark = "早退范围"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "classesObjId", length = 30, remark = "部门ID orgId"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "classesObjName", length = 64, remark = "部门名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "classesId", length = 30, remark = "班级ID"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/attendanceClasses.listAttendanceClassess?page=1&row=1&classesId=842022081548770433",
        resBody="{\"code\":0,\"data\":[{\"attrs\":[{\"attrId\":\"112022110409750012\",\"classesId\":\"102022110491120003\",\"name\":\"上午上班\",\"page\":-1,\"records\":0,\"row\":0,\"specCd\":\"10000\",\"statusCd\":\"0\",\"storeId\":\"102022081507340423\",\"total\":0,\"value\":\"\"},{\"attrId\":\"112022110496120014\",\"classesId\":\"102022110491120003\",\"name\":\"下午下班\",\"page\":-1,\"records\":0,\"row\":0,\"specCd\":\"20000\",\"statusCd\":\"0\",\"storeId\":\"102022081507340423\",\"total\":0,\"value\":\"\"}],\"classesId\":\"102022110491120003\",\"classesName\":\"测试考勤设置\",\"classesObjId\":\"842022081548770433\",\"classesObjName\":\"演示物业\",\"classesObjType\":\"1003\",\"clockCount\":\"2\",\"clockType\":\"1001\",\"clockTypeName\":\"每天打卡\",\"clockTypeValue\":\"*\",\"lateOffset\":\"30\",\"leaveOffset\":\"31\",\"page\":-1,\"records\":0,\"row\":0,\"statusCd\":\"0\",\"storeId\":\"102022081507340423\",\"timeOffset\":\"30\",\"total\":0}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)
/**
 * 类表述：查询
 * 服务编码：attendanceClasses.listAttendanceClasses
 * 请求路劲：/app/attendanceClasses.ListAttendanceClasses
 * add by 吴学文 at 2022-07-16 17:50:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "attendanceClasses.listAttendanceClassess")
public class ListAttendanceClassessCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListAttendanceClassessCmd.class);
    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           AttendanceClassesDto attendanceClassesDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesDto.class);

           int count = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassessCount(attendanceClassesDto);

           List<AttendanceClassesDto> attendanceClassesDtos = null;

           if (count > 0) {
               attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);
           } else {
               attendanceClassesDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, attendanceClassesDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
