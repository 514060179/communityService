package com.newland.property.common.bmo.hcGovTranslate.impl;

import com.newland.property.common.bmo.hcGovTranslate.IUpdateHcGovTranslateBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IHcGovTranslateInnerServiceSMO;
import com.newland.property.po.hcGovTranslate.HcGovTranslatePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateHcGovTranslateBMOImpl")
public class UpdateHcGovTranslateBMOImpl implements IUpdateHcGovTranslateBMO {

    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    /**
     *
     *
     * @param hcGovTranslatePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(HcGovTranslatePo hcGovTranslatePo) {

        int flag = hcGovTranslateInnerServiceSMOImpl.updateHcGovTranslate(hcGovTranslatePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
