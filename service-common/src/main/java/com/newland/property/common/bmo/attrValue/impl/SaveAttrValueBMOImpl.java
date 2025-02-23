package com.newland.property.common.bmo.attrValue.impl;

import com.newland.property.common.bmo.attrValue.ISaveAttrValueBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IAttrValueInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrValuePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAttrValueBMOImpl")
public class SaveAttrValueBMOImpl implements ISaveAttrValueBMO {

    @Autowired
    private IAttrValueInnerServiceSMO attrValueInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attrValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AttrValuePo attrValuePo) {

        attrValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        int flag = attrValueInnerServiceSMOImpl.saveAttrValue(attrValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
