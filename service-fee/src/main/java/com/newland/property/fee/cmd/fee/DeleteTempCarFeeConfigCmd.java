package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.ITempCarFeeConfigAttrV1InnerServiceSMO;
import com.newland.property.intf.fee.ITempCarFeeConfigV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeeConfigPo;
import com.newland.property.po.tempCarFee.TempCarFeeConfigPo;
import com.newland.property.po.tempCarFee.TempCarFeeConfigAttrPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "fee.deleteTempCarFeeConfig")
public class DeleteTempCarFeeConfigCmd extends Cmd {

    @Autowired
    private ITempCarFeeConfigV1InnerServiceSMO tempCarFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrV1InnerServiceSMO tempCarFeeConfigAttrV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigPo.class);
        int flag = tempCarFeeConfigV1InnerServiceSMOImpl.deleteTempCarFeeConfig(tempCarFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("删除临时车费失败");
        }
        JSONArray attrs = reqJson.getJSONArray("tempCarFeeConfigAttrs");
        if (attrs != null && attrs.size() > 0) {
            JSONObject attr = null;
            for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
                attr = attrs.getJSONObject(attrIndex);
                attr.put("ruleId", reqJson.getString("ruleId"));
                attr.put("communityId", reqJson.getString("communityId"));
                if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                    continue;
                }
                TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(attr, TempCarFeeConfigAttrPo.class);
                flag = tempCarFeeConfigAttrV1InnerServiceSMOImpl.deleteTempCarFeeConfigAttr(tempCarFeeConfigAttrPo);
                if (flag < 1) {
                    throw new CmdException("删除临时车费失败");
                }
            }
        }
        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setConfigId(reqJson.getString("feeConfigId"));
        payFeeConfigPo.setStatusCd("1");
        int i = feeConfigInnerServiceSMOImpl.deleteFeeConfig(payFeeConfigPo);
        if (i < 1) {
            throw new CmdException("删除费用项失败");
        }
    }
}
