package com.newland.property.fee.bmo.feePrintSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feePrintSpec.IUpdateFeePrintSpecBMO;
import com.newland.property.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.newland.property.po.fee.feePrintSpec.FeePrintSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeePrintSpecBMOImpl")
public class UpdateFeePrintSpecBMOImpl implements IUpdateFeePrintSpecBMO {

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    /**
     * @param feePrintSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeePrintSpecPo feePrintSpecPo) {

        int flag = feePrintSpecInnerServiceSMOImpl.updateFeePrintSpec(feePrintSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
