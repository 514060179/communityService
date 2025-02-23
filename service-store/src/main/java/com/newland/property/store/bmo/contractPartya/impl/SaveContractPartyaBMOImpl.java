package com.newland.property.store.bmo.contractPartya.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractPartyaInnerServiceSMO;
import com.newland.property.po.contract.ContractPartyaPo;
import com.newland.property.store.bmo.contractPartya.ISaveContractPartyaBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractPartyaBMOImpl")
public class SaveContractPartyaBMOImpl implements ISaveContractPartyaBMO {

    @Autowired
    private IContractPartyaInnerServiceSMO contractPartyaInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractPartyaPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractPartyaPo contractPartyaPo) {

        contractPartyaPo.setPartyaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_partyaId));
        int flag = contractPartyaInnerServiceSMOImpl.saveContractPartya(contractPartyaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
