package com.newland.property.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeeConfigPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "feeConfig.saveFeeConfig")
public class SaveFeeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "必填，请选择费用类型");
        Assert.hasKeyAndValue(reqJson, "feeName", "必填，请填写收费项目");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "必填，请选择费用标识");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择计费起始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择计费终止时间");
        Assert.hasKeyAndValue(reqJson, "computingFormula", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "squarePrice", "必填，请填写计费单价");
        Assert.hasKeyAndValue(reqJson, "additionalAmount", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
        Assert.hasKeyAndValue(reqJson, "paymentCd", "付费类型不能为空");
        Assert.hasKeyAndValue(reqJson, "paymentCycle", "缴费周期不能为空");

        // todo 这里校验费用名称不能重复，因为很多物业建相同名字的费用后自己都分不清然后 随便删了一个导致系统有问题


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeName(reqJson.getString("feeName"));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setIsDefault("F");
        int count = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigsCount(feeConfigDto);

        if(count > 0){
            throw new CmdException(reqJson.getString("feeName")+"已存在");
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        reqJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        reqJson.put("isDefault", "F");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigPo.class);

        int flag = payFeeConfigV1InnerServiceSMOImpl.savePayFeeConfig(payFeeConfigPo);

        if (flag < 1) {
            throw new CmdException("保存费用项失败");
        }

        // TODO 生成账单后发推送-jpush
        // appPage: /pages/fee/oweFee

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
