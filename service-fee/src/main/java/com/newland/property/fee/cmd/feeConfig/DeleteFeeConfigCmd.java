package com.newland.property.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeeConfigPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "feeConfig.deleteFeeConfig")
public class DeleteFeeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "configId", "费用项ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        feeConfigDto.setIsDefault("T");
        int feeCount = feeConfigInnerServiceSMOImpl.queryFeeConfigsCount(feeConfigDto);
        if (feeCount > 0) {
            throw new IllegalArgumentException("该费用项目不能删除");
        }


    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigPo.class);

        int flag = payFeeConfigV1InnerServiceSMOImpl.deletePayFeeConfig(payFeeConfigPo);

        if (flag < 1) {
            throw new CmdException("删除费用项失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
