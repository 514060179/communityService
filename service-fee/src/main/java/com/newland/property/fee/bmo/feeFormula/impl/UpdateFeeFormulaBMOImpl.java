package com.newland.property.fee.bmo.feeFormula.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeFormula.IUpdateFeeFormulaBMO;
import com.newland.property.intf.fee.IFeeFormulaInnerServiceSMO;
import com.newland.property.po.fee.FeeFormulaPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeFormulaBMOImpl")
public class UpdateFeeFormulaBMOImpl implements IUpdateFeeFormulaBMO {

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    /**
     * @param feeFormulaPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeFormulaPo feeFormulaPo) {

        int flag = feeFormulaInnerServiceSMOImpl.updateFeeFormula(feeFormulaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
