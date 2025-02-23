package com.newland.property.acct.bmo.accountBank.impl;

import com.newland.property.acct.bmo.accountBank.ISaveAccountBankBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;

import com.newland.property.intf.acct.IAccountBankInnerServiceSMO;
import com.newland.property.po.account.AccountBankPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBankBMOImpl")
public class SaveAccountBankBMOImpl implements ISaveAccountBankBMO {

    @Autowired
    private IAccountBankInnerServiceSMO accountBankInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBankPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AccountBankPo accountBankPo) {

        accountBankPo.setBankId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bankId));
        int flag = accountBankInnerServiceSMOImpl.saveAccountBank(accountBankPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
