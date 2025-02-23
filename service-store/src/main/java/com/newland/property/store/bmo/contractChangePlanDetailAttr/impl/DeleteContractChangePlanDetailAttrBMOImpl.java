package com.newland.property.store.bmo.contractChangePlanDetailAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractChangePlanDetailAttrInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanDetailAttrPo;
import com.newland.property.store.bmo.contractChangePlanDetailAttr.IDeleteContractChangePlanDetailAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractChangePlanDetailAttrBMOImpl")
public class DeleteContractChangePlanDetailAttrBMOImpl implements IDeleteContractChangePlanDetailAttrBMO {

    @Autowired
    private IContractChangePlanDetailAttrInnerServiceSMO contractChangePlanDetailAttrInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDetailAttrPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {

        int flag = contractChangePlanDetailAttrInnerServiceSMOImpl.deleteContractChangePlanDetailAttr(contractChangePlanDetailAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
