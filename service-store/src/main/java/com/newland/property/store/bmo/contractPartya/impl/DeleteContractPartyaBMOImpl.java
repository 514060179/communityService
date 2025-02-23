package com.newland.property.store.bmo.contractPartya.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractPartyaInnerServiceSMO;
import com.newland.property.po.contract.ContractPartyaPo;
import com.newland.property.store.bmo.contractPartya.IDeleteContractPartyaBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractPartyaBMOImpl")
public class DeleteContractPartyaBMOImpl implements IDeleteContractPartyaBMO {

    @Autowired
    private IContractPartyaInnerServiceSMO contractPartyaInnerServiceSMOImpl;

    /**
     * @param contractPartyaPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractPartyaPo contractPartyaPo) {

        int flag = contractPartyaInnerServiceSMOImpl.deleteContractPartya(contractPartyaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
