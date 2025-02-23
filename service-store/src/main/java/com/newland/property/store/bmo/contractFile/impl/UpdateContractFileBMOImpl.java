package com.newland.property.store.bmo.contractFile.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractFileInnerServiceSMO;
import com.newland.property.po.contract.ContractFilePo;
import com.newland.property.store.bmo.contractFile.IUpdateContractFileBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractFileBMOImpl")
public class UpdateContractFileBMOImpl implements IUpdateContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     *
     *
     * @param contractFilePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ContractFilePo contractFilePo) {

        int flag = contractFileInnerServiceSMOImpl.updateContractFile(contractFilePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
