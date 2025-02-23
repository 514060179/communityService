package com.newland.property.store.bmo.contractCollectionPlan.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractCollectionPlanInnerServiceSMO;
import com.newland.property.po.contract.ContractCollectionPlanPo;
import com.newland.property.store.bmo.contractCollectionPlan.ISaveContractCollectionPlanBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractCollectionPlanBMOImpl")
public class SaveContractCollectionPlanBMOImpl implements ISaveContractCollectionPlanBMO {

    @Autowired
    private IContractCollectionPlanInnerServiceSMO contractCollectionPlanInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractCollectionPlanPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractCollectionPlanPo contractCollectionPlanPo) {

        contractCollectionPlanPo.setPlanId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_planId));
        int flag = contractCollectionPlanInnerServiceSMOImpl.saveContractCollectionPlan(contractCollectionPlanPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
