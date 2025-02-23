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
package com.newland.property.fee.cmd.payFeeRule;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.payFeeRuleBill.PayFeeRuleBillDto;
import com.newland.property.fee.feeMonth.IPayFeeMonth;
import com.newland.property.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeRuleBillV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.intf.report.IReportOweFeeInnerServiceSMO;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.payFeeRule.PayFeeRulePo;
import com.newland.property.po.payFeeRuleBill.PayFeeRuleBillPo;
import com.newland.property.po.reportFee.ReportOweFeePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：payFeeRule.deletePayFeeRule
 * 请求路劲：/app/payFeeRule.DeletePayFeeRule
 * add by 吴学文 at 2023-10-18 18:08:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "payFeeRule.deletePayFeeRule")
public class DeletePayFeeRuleCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeletePayFeeRuleCmd.class);

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleBillV1InnerServiceSMO payFeeRuleBillV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Environment.isDevEnv();
        Assert.hasKeyAndValue(reqJson, "ruleId", "ruleId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PayFeeRulePo payFeeRulePo = BeanConvertUtil.covertBean(reqJson, PayFeeRulePo.class);
        int flag = payFeeRuleV1InnerServiceSMOImpl.deletePayFeeRule(payFeeRulePo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        //todo 查询账单费用
        PayFeeRuleBillDto payFeeRuleBillDto = new PayFeeRuleBillDto();
        payFeeRuleBillDto.setRuleId(reqJson.getString("ruleId"));
        payFeeRuleBillDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeRuleBillDto> ruleBillDtos = payFeeRuleBillV1InnerServiceSMOImpl.queryPayFeeRuleBills(payFeeRuleBillDto);

        if (ruleBillDtos == null || ruleBillDtos.isEmpty()) {
            return;
        }
        PayFeeRuleBillPo payFeeRuleBillPo = null;
        //todo 删除 账单
        payFeeRuleBillPo = new PayFeeRuleBillPo();
        payFeeRuleBillPo.setRuleId(reqJson.getString("ruleId"));
        payFeeRuleBillV1InnerServiceSMOImpl.deletePayFeeRuleBill(payFeeRuleBillPo);

        PayFeePo payFeePo = null;
        PayFeeDetailPo payFeeDetailPo = null;
        for (PayFeeRuleBillDto tmpPayFeeRuleBillDto : ruleBillDtos) {
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(tmpPayFeeRuleBillDto.getFeeId());
            payFeePo.setCommunityId(tmpPayFeeRuleBillDto.getCommunityId());
            payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);

            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setFeeId(tmpPayFeeRuleBillDto.getFeeId());
            payFeeDetailPo.setCommunityId(tmpPayFeeRuleBillDto.getCommunityId());

            payFeeDetailV1InnerServiceSMOImpl.deletePayFeeDetailNew(payFeeDetailPo);

            // todo 删除欠费信息
            ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
            reportOweFeePo.setFeeId(payFeePo.getFeeId());
            reportOweFeePo.setCommunityId(payFeePo.getCommunityId());
            reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

            //todo 离散的月
            payFeeMonthImpl.deleteFeeMonth(payFeePo.getFeeId(),payFeePo.getCommunityId());
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
