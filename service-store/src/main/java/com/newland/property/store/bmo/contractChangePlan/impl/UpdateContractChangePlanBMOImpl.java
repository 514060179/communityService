package com.newland.property.store.bmo.contractChangePlan.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractChangePlanInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanPo;
import com.newland.property.store.bmo.contractChangePlan.IUpdateContractChangePlanBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanBMOImpl")
public class UpdateContractChangePlanBMOImpl implements IUpdateContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    /**
     * @param contractChangePlanPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ContractChangePlanPo contractChangePlanPo) {

        int flag = contractChangePlanInnerServiceSMOImpl.updateContractChangePlan(contractChangePlanPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
