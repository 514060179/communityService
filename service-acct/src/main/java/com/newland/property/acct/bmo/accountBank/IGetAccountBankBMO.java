package com.newland.property.acct.bmo.accountBank;
import com.newland.property.dto.account.AccountBankDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBankBMO {


    /**
     * 查询开户行
     * add by wuxw
     * @param  accountBankDto
     * @return
     */
    ResponseEntity<String> get(AccountBankDto accountBankDto);


}
