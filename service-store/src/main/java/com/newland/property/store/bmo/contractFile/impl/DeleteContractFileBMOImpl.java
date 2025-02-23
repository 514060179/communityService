package com.newland.property.store.bmo.contractFile.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractFileInnerServiceSMO;
import com.newland.property.po.contract.ContractFilePo;
import com.newland.property.store.bmo.contractFile.IDeleteContractFileBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractFileBMOImpl")
public class DeleteContractFileBMOImpl implements IDeleteContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     * @param contractFilePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractFilePo contractFilePo) {

        int flag = contractFileInnerServiceSMOImpl.deleteContractFile(contractFilePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
