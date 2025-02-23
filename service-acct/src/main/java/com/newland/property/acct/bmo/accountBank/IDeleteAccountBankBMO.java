package com.newland.property.acct.bmo.accountBank;
import com.newland.property.po.account.AccountBankPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAccountBankBMO {


    /**
     * 修改开户行
     * add by wuxw
     * @param accountBankPo
     * @return
     */
    ResponseEntity<String> delete(AccountBankPo accountBankPo);


}
