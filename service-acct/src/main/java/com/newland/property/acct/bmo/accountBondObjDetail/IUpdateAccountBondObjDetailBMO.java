package com.newland.property.acct.bmo.accountBondObjDetail;
import com.newland.property.po.account.AccountBondObjDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountBondObjDetailBMO {


    /**
     * 修改保证金明细
     * add by wuxw
     * @param accountBondObjDetailPo
     * @return
     */
    ResponseEntity<String> update(AccountBondObjDetailPo accountBondObjDetailPo);


}
