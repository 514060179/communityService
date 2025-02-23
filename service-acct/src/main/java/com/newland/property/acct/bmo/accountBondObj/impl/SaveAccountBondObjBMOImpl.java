package com.newland.property.acct.bmo.accountBondObj.impl;

import com.newland.property.acct.bmo.accountBondObj.ISaveAccountBondObjBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.acct.IAccountBondObjInnerServiceSMO;
import com.newland.property.po.account.AccountBondObjPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBondObjBMOImpl")
public class SaveAccountBondObjBMOImpl implements ISaveAccountBondObjBMO {

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBondObjPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AccountBondObjPo accountBondObjPo) {

        accountBondObjPo.setBobjId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bobjId));
        int flag = accountBondObjInnerServiceSMOImpl.saveAccountBondObj(accountBondObjPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
