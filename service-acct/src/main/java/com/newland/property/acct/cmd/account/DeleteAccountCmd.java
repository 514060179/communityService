package com.newland.property.acct.cmd.account;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.acct.IAccountInnerServiceSMO;
import com.newland.property.po.account.AccountPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "account.deleteAccount")
public class DeleteAccountCmd  extends Cmd {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "acctId", "acctId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AccountPo accountPo = BeanConvertUtil.covertBean(reqJson, AccountPo.class);
        int flag = accountInnerServiceSMOImpl.updateAccount(accountPo);

        if(flag < 1){
            throw new CmdException("更新失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
