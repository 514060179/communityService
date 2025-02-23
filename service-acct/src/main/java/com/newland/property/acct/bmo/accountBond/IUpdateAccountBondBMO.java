package com.newland.property.acct.bmo.accountBond;
import com.newland.property.po.account.AccountBondPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountBondBMO {


    /**
     * 修改保证金
     * add by wuxw
     * @param accountBondPo
     * @return
     */
    ResponseEntity<String> update(AccountBondPo accountBondPo);


}
