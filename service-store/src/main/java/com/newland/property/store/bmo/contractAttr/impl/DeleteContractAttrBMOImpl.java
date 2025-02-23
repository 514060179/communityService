package com.newland.property.store.bmo.contractAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractAttrInnerServiceSMO;
import com.newland.property.po.contract.ContractAttrPo;
import com.newland.property.store.bmo.contractAttr.IDeleteContractAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractAttrBMOImpl")
public class DeleteContractAttrBMOImpl implements IDeleteContractAttrBMO {

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    /**
     * @param contractAttrPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractAttrPo contractAttrPo) {

        int flag = contractAttrInnerServiceSMOImpl.deleteContractAttr(contractAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
