package com.newland.property.acct.bmo.accountBondObj;
import com.newland.property.po.account.AccountBondObjPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAccountBondObjBMO {


    /**
     * 修改保证金对象
     * add by wuxw
     * @param accountBondObjPo
     * @return
     */
    ResponseEntity<String> delete(AccountBondObjPo accountBondObjPo);


}
