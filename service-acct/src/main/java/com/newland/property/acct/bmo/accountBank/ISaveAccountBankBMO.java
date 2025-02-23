package com.newland.property.acct.bmo.accountBank;

import com.newland.property.po.account.AccountBankPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBankBMO {


    /**
     * 添加开户行
     * add by wuxw
     * @param accountBankPo
     * @return
     */
    ResponseEntity<String> save(AccountBankPo accountBankPo);


}
