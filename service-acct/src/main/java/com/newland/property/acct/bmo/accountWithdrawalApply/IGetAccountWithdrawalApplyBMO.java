package com.newland.property.acct.bmo.accountWithdrawalApply;
import com.newland.property.dto.account.AccountWithdrawalApplyDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountWithdrawalApplyBMO {


    /**
     * 查询账户提现
     * add by wuxw
     * @param  accountWithdrawalApplyDto
     * @return
     */
    ResponseEntity<String> get(AccountWithdrawalApplyDto accountWithdrawalApplyDto);

    /**
     * 查询账户提现
     * add by wuxw
     * @param
     * @return
     */
    ResponseEntity<String> listStateWithdrawalApplys(String [] states,int page,int row);


}
