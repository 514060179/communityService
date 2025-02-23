package com.newland.property.acct.bmo.accountBond;

import com.newland.property.po.account.AccountBondPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBondBMO {


    /**
     * 添加保证金
     * add by wuxw
     * @param accountBondPo
     * @return
     */
    ResponseEntity<String> save(AccountBondPo accountBondPo);


}
