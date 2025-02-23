package com.newland.property.store.bmo.contractTypeSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractTypeSpecInnerServiceSMO;
import com.newland.property.po.contract.ContractTypeSpecPo;
import com.newland.property.store.bmo.contractTypeSpec.ISaveContractTypeSpecBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractTypeSpecBMOImpl")
public class SaveContractTypeSpecBMOImpl implements ISaveContractTypeSpecBMO {

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractTypeSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractTypeSpecPo contractTypeSpecPo) {

        contractTypeSpecPo.setSpecCd(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specCd));
        int flag = contractTypeSpecInnerServiceSMOImpl.saveContractTypeSpec(contractTypeSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
