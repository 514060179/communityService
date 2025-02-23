package com.newland.property.store.bmo.contractChangePlan.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IContractChangeUserInnerServiceSMO;
import com.newland.property.intf.store.IContractChangePlanInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanPo;
import com.newland.property.store.bmo.contractChangePlan.IDeleteContractChangePlanBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractChangePlanBMOImpl")
public class DeleteContractChangePlanBMOImpl implements IDeleteContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMO;

    /**
     * @param contractChangePlanPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractChangePlanPo contractChangePlanPo) {

        int flag = contractChangePlanInnerServiceSMOImpl.deleteContractChangePlan(contractChangePlanPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        contractChangeUserInnerServiceSMO.deleteTask(contractChangePlanPo);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
