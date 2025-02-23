package com.newland.property.acct.bmo.accountBank.impl;

import com.newland.property.acct.bmo.accountBank.IGetAccountBankBMO;
import com.newland.property.intf.acct.IAccountBankInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.newland.property.dto.account.AccountBankDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountBankBMOImpl")
public class GetAccountBankBMOImpl implements IGetAccountBankBMO {

    @Autowired
    private IAccountBankInnerServiceSMO accountBankInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountBankDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AccountBankDto accountBankDto) {


        int count = accountBankInnerServiceSMOImpl.queryAccountBanksCount(accountBankDto);

        List<AccountBankDto> accountBankDtos = null;
        if (count > 0) {
            accountBankDtos = accountBankInnerServiceSMOImpl.queryAccountBanks(accountBankDto);
        } else {
            accountBankDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountBankDto.getRow()), count, accountBankDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
