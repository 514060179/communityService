package com.newland.property.acct.bmo.accountBank.impl;

import com.newland.property.acct.bmo.accountBank.IDeleteAccountBankBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.IAccountBankInnerServiceSMO;
import com.newland.property.po.account.AccountBankPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAccountBankBMOImpl")
public class DeleteAccountBankBMOImpl implements IDeleteAccountBankBMO {

    @Autowired
    private IAccountBankInnerServiceSMO accountBankInnerServiceSMOImpl;

    /**
     * @param accountBankPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AccountBankPo accountBankPo) {

        int flag = accountBankInnerServiceSMOImpl.deleteAccountBank(accountBankPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
