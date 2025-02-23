package com.newland.property.acct.bmo.accountWithdrawalApply.impl;

import com.newland.property.acct.bmo.accountWithdrawalApply.IDeleteAccountWithdrawalApplyBMO;
import com.newland.property.core.annotation.PropertyTransactional;

import com.newland.property.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.newland.property.po.account.AccountWithdrawalApplyPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteAccountWithdrawalApplyBMOImpl")
public class DeleteAccountWithdrawalApplyBMOImpl implements IDeleteAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     * @param accountWithdrawalApplyPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AccountWithdrawalApplyPo accountWithdrawalApplyPo) {

        int flag = accountWithdrawalApplyInnerServiceSMOImpl.deleteAccountWithdrawalApply(accountWithdrawalApplyPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
