package com.newland.property.store.bmo.contractTypeTemplate.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractTypeTemplateInnerServiceSMO;
import com.newland.property.po.contract.ContractTypeTemplatePo;
import com.newland.property.store.bmo.contractTypeTemplate.IUpdateContractTypeTemplateBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractTypeTemplateBMOImpl")
public class UpdateContractTypeTemplateBMOImpl implements IUpdateContractTypeTemplateBMO {

    @Autowired
    private IContractTypeTemplateInnerServiceSMO contractTypeTemplateInnerServiceSMOImpl;

    /**
     * @param contractTypeTemplatePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ContractTypeTemplatePo contractTypeTemplatePo) {

        int flag = contractTypeTemplateInnerServiceSMOImpl.updateContractTypeTemplate(contractTypeTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
