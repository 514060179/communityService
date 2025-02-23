package com.newland.property.fee.bmo.feePrintSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feePrintSpec.ISaveFeePrintSpecBMO;
import com.newland.property.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.newland.property.po.fee.feePrintSpec.FeePrintSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeePrintSpecBMOImpl")
public class SaveFeePrintSpecBMOImpl implements ISaveFeePrintSpecBMO {

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feePrintSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeePrintSpecPo feePrintSpecPo) {

        feePrintSpecPo.setPrintId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_printId));
        int flag = feePrintSpecInnerServiceSMOImpl.saveFeePrintSpec(feePrintSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
