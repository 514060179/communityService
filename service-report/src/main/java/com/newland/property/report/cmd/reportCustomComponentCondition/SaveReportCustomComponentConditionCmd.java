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
package com.newland.property.report.cmd.reportCustomComponentCondition;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportCustomComponentConditionV1InnerServiceSMO;
import com.newland.property.po.reportCustom.ReportCustomComponentConditionPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：reportCustomComponentCondition.saveReportCustomComponentCondition
 * 请求路劲：/app/reportCustomComponentCondition.SaveReportCustomComponentCondition
 * add by 吴学文 at 2021-11-09 13:33:12 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "reportCustomComponentCondition.saveReportCustomComponentCondition")
public class SaveReportCustomComponentConditionCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReportCustomComponentConditionCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReportCustomComponentConditionV1InnerServiceSMO reportCustomComponentConditionV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "componentId", "请求报文中未包含componentId");
Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
Assert.hasKeyAndValue(reqJson, "holdpace", "请求报文中未包含holdpace");
Assert.hasKeyAndValue(reqJson, "param", "请求报文中未包含param");
Assert.hasKeyAndValue(reqJson, "type", "请求报文中未包含type");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       ReportCustomComponentConditionPo reportCustomComponentConditionPo = BeanConvertUtil.covertBean(reqJson, ReportCustomComponentConditionPo.class);
        reportCustomComponentConditionPo.setConditionId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = reportCustomComponentConditionV1InnerServiceSMOImpl.saveReportCustomComponentCondition(reportCustomComponentConditionPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
