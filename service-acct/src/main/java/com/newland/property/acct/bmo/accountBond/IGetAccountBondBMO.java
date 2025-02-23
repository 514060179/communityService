package com.newland.property.acct.bmo.accountBond;
import com.newland.property.dto.account.AccountBondDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBondBMO {


    /**
     * 查询保证金
     * add by wuxw
     * @param  accountBondDto
     * @return
     */
    ResponseEntity<String> get(AccountBondDto accountBondDto);


}
