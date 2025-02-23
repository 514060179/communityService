package com.newland.property.store.bmo.contractTypeSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractTypeSpecInnerServiceSMO;
import com.newland.property.po.contract.ContractTypeSpecPo;
import com.newland.property.store.bmo.contractTypeSpec.IDeleteContractTypeSpecBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractTypeSpecBMOImpl")
public class DeleteContractTypeSpecBMOImpl implements IDeleteContractTypeSpecBMO {

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;

    /**
     * @param contractTypeSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractTypeSpecPo contractTypeSpecPo) {

        int flag = contractTypeSpecInnerServiceSMOImpl.deleteContractTypeSpec(contractTypeSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
