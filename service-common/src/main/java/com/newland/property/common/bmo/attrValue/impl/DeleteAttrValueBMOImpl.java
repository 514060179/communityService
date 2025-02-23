package com.newland.property.common.bmo.attrValue.impl;

import com.newland.property.common.bmo.attrValue.IDeleteAttrValueBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttrValueInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrValuePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttrValueBMOImpl")
public class DeleteAttrValueBMOImpl implements IDeleteAttrValueBMO {

    @Autowired
    private IAttrValueInnerServiceSMO attrValueInnerServiceSMOImpl;

    /**
     * @param attrValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AttrValuePo attrValuePo) {

        int flag = attrValueInnerServiceSMOImpl.deleteAttrValue(attrValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
