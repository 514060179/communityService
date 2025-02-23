package com.newland.property.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.mall.IMallCommonApiBmo;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.account.AccountDto;
import com.newland.property.intf.acct.IAccountInnerServiceSMO;
import com.newland.property.po.account.AccountDetailPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("prestoreAccountImpl")
public class PrestoreAccountImpl implements IMallCommonApiBmo {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ownerId", "未包含业主");
        Assert.hasKeyAndValue(reqJson, "link", "未包含业主手机号");
        Assert.hasKeyAndValue(reqJson, "amount", "未包含金额");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {


        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(reqJson.getString("ownerId"));
        accountDto.setLink(reqJson.getString("link"));
        accountDto.setPartId(reqJson.getString("communityId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (ListUtil.isNull(accountDtos)) {
            throw new CmdException("账户不存在");
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setRemark("金币-抵扣金充值");
        accountDetailPo.setAmount(reqJson.getString("amount"));
        accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);

        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "充值成功"));

    }
}
