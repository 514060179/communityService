package com.newland.property.store.bmo.contractAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractAttrInnerServiceSMO;
import com.newland.property.po.contract.ContractAttrPo;
import com.newland.property.store.bmo.contractAttr.ISaveContractAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractAttrBMOImpl")
public class SaveContractAttrBMOImpl implements ISaveContractAttrBMO {

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractAttrPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractAttrPo contractAttrPo) {

        contractAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        int flag = contractAttrInnerServiceSMOImpl.saveContractAttr(contractAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
