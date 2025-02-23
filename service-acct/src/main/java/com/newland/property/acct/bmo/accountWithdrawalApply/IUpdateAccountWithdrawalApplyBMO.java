package com.newland.property.acct.bmo.accountWithdrawalApply;
import com.newland.property.po.account.AccountWithdrawalApplyPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountWithdrawalApplyBMO {


    /**
     * 修改账户提现
     * add by wuxw
     * @param accountWithdrawalApplyPo
     * @return
     */
    ResponseEntity<String> update(AccountWithdrawalApplyPo accountWithdrawalApplyPo);


}
