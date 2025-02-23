package com.newland.property.acct.bmo.accountBondObj.impl;

import com.newland.property.acct.bmo.accountBondObj.IDeleteAccountBondObjBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.IAccountBondObjInnerServiceSMO;
import com.newland.property.po.account.AccountBondObjPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAccountBondObjBMOImpl")
public class DeleteAccountBondObjBMOImpl implements IDeleteAccountBondObjBMO {

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    /**
     * @param accountBondObjPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AccountBondObjPo accountBondObjPo) {

        int flag = accountBondObjInnerServiceSMOImpl.deleteAccountBondObj(accountBondObjPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
