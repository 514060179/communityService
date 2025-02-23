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
package com.newland.property.common.cmd.printer;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.intf.common.IPrinterRuleFeeV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.newland.property.po.printer.PrinterRuleFeePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：printerRuleFee.savePrinterRuleFee
 * 请求路劲：/app/printerRuleFee.SavePrinterRuleFee
 * add by 吴学文 at 2023-02-17 14:51:51 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "printer.savePrinterRuleFee")
public class SavePrinterRuleFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SavePrinterRuleFeeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IPrinterRuleFeeV1InnerServiceSMO printerRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("feeId"));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
       List<FeeConfigDto> feeConfigDtos =  payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

       Assert.listOnlyOne(feeConfigDtos,"费用项不存在");
       reqJson.put("feeConfigName",feeConfigDtos.get(0).getFeeName());
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PrinterRuleFeePo printerRuleFeePo = BeanConvertUtil.covertBean(reqJson, PrinterRuleFeePo.class);
        printerRuleFeePo.setPrfId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = printerRuleFeeV1InnerServiceSMOImpl.savePrinterRuleFee(printerRuleFeePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
