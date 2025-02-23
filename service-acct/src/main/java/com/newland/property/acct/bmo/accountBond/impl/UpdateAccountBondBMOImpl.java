package com.newland.property.acct.bmo.accountBond.impl;


import com.newland.property.acct.bmo.accountBond.IUpdateAccountBondBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.IAccountBondInnerServiceSMO;
import com.newland.property.po.account.AccountBondPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAccountBondBMOImpl")
public class UpdateAccountBondBMOImpl implements IUpdateAccountBondBMO {

    @Autowired
    private IAccountBondInnerServiceSMO accountBondInnerServiceSMOImpl;

    /**
     *
     *
     * @param accountBondPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AccountBondPo accountBondPo) {

        int flag = accountBondInnerServiceSMOImpl.updateAccountBond(accountBondPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
