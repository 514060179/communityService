package com.newland.property.store.bmo.contractChangePlanDetailAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractChangePlanDetailAttrInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanDetailAttrPo;
import com.newland.property.store.bmo.contractChangePlanDetailAttr.ISaveContractChangePlanDetailAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractChangePlanDetailAttrBMOImpl")
public class SaveContractChangePlanDetailAttrBMOImpl implements ISaveContractChangePlanDetailAttrBMO {

    @Autowired
    private IContractChangePlanDetailAttrInnerServiceSMO contractChangePlanDetailAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanDetailAttrPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {

        contractChangePlanDetailAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        int flag = contractChangePlanDetailAttrInnerServiceSMOImpl.saveContractChangePlanDetailAttr(contractChangePlanDetailAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
