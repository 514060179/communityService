package com.newland.property.acct.bmo.accountBondObjDetail.impl;

import com.newland.property.acct.bmo.accountBondObjDetail.ISaveAccountBondObjDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;

import com.newland.property.intf.acct.IAccountBondObjDetailInnerServiceSMO;
import com.newland.property.po.account.AccountBondObjDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBondObjDetailBMOImpl")
public class SaveAccountBondObjDetailBMOImpl implements ISaveAccountBondObjDetailBMO {

    @Autowired
    private IAccountBondObjDetailInnerServiceSMO accountBondObjDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBondObjDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AccountBondObjDetailPo accountBondObjDetailPo) {

        accountBondObjDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = accountBondObjDetailInnerServiceSMOImpl.saveAccountBondObjDetail(accountBondObjDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
