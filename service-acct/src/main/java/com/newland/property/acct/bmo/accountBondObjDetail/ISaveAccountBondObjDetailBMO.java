package com.newland.property.acct.bmo.accountBondObjDetail;

import com.newland.property.po.account.AccountBondObjDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBondObjDetailBMO {


    /**
     * 添加保证金明细
     * add by wuxw
     * @param accountBondObjDetailPo
     * @return
     */
    ResponseEntity<String> save(AccountBondObjDetailPo accountBondObjDetailPo);


}
