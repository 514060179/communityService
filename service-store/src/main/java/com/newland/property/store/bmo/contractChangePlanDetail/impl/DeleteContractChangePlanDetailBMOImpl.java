package com.newland.property.store.bmo.contractChangePlanDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanDetailPo;
import com.newland.property.store.bmo.contractChangePlanDetail.IDeleteContractChangePlanDetailBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractChangePlanDetailBMOImpl")
public class DeleteContractChangePlanDetailBMOImpl implements IDeleteContractChangePlanDetailBMO {

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractChangePlanDetailPo contractChangePlanDetailPo) {

        int flag = contractChangePlanDetailInnerServiceSMOImpl.deleteContractChangePlanDetail(contractChangePlanDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
