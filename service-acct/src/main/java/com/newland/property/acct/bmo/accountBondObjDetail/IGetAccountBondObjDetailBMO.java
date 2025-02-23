package com.newland.property.acct.bmo.accountBondObjDetail;
import com.newland.property.dto.account.AccountBondObjDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBondObjDetailBMO {


    /**
     * 查询保证金明细
     * add by wuxw
     * @param  accountBondObjDetailDto
     * @return
     */
    ResponseEntity<String> get(AccountBondObjDetailDto accountBondObjDetailDto);


}
