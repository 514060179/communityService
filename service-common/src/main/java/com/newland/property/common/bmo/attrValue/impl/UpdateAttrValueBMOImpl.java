package com.newland.property.common.bmo.attrValue.impl;

import com.newland.property.common.bmo.attrValue.IUpdateAttrValueBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttrValueInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrValuePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttrValueBMOImpl")
public class UpdateAttrValueBMOImpl implements IUpdateAttrValueBMO {

    @Autowired
    private IAttrValueInnerServiceSMO attrValueInnerServiceSMOImpl;

    /**
     * @param attrValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AttrValuePo attrValuePo) {

        int flag = attrValueInnerServiceSMOImpl.updateAttrValue(attrValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
