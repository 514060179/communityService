package com.newland.property.store.bmo.contractType.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractTypeInnerServiceSMO;
import com.newland.property.po.contract.ContractTypePo;
import com.newland.property.store.bmo.contractType.IUpdateContractTypeBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractTypeBMOImpl")
public class UpdateContractTypeBMOImpl implements IUpdateContractTypeBMO {

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    /**
     * @param contractTypePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ContractTypePo contractTypePo) {

        int flag = contractTypeInnerServiceSMOImpl.updateContractType(contractTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
