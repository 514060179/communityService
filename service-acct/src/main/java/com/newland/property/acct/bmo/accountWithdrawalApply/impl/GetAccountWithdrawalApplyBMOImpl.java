package com.newland.property.acct.bmo.accountWithdrawalApply.impl;

import com.newland.property.acct.bmo.accountWithdrawalApply.IGetAccountWithdrawalApplyBMO;
import com.newland.property.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.newland.property.dto.account.AccountWithdrawalApplyDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountWithdrawalApplyBMOImpl")
public class GetAccountWithdrawalApplyBMOImpl implements IGetAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountWithdrawalApplyDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AccountWithdrawalApplyDto accountWithdrawalApplyDto) {


        int count = accountWithdrawalApplyInnerServiceSMOImpl.queryAccountWithdrawalApplysCount(accountWithdrawalApplyDto);

        List<AccountWithdrawalApplyDto> accountWithdrawalApplyDtos = null;
        if (count > 0) {
            accountWithdrawalApplyDtos = accountWithdrawalApplyInnerServiceSMOImpl.queryAccountWithdrawalApplys(accountWithdrawalApplyDto);
        } else {
            accountWithdrawalApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountWithdrawalApplyDto.getRow()), count, accountWithdrawalApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listStateWithdrawalApplys(String[] states, int page, int row) {
        int count = accountWithdrawalApplyInnerServiceSMOImpl.listStateWithdrawalApplysCount( states);

        List<AccountWithdrawalApplyDto> accountWithdrawalApplyDtos = null;
        if (count > 0) {
            accountWithdrawalApplyDtos = accountWithdrawalApplyInnerServiceSMOImpl.listStateWithdrawalApplys(states);
        } else {
            accountWithdrawalApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) page), count, accountWithdrawalApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
