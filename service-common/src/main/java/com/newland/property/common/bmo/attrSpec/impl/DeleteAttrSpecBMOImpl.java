package com.newland.property.common.bmo.attrSpec.impl;

import com.newland.property.common.bmo.attrSpec.IDeleteAttrSpecBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAttrSpecInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttrSpecBMOImpl")
public class DeleteAttrSpecBMOImpl implements IDeleteAttrSpecBMO {

    @Autowired
    private IAttrSpecInnerServiceSMO attrSpecInnerServiceSMOImpl;

    /**
     * @param attrSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AttrSpecPo attrSpecPo) {

        int flag = attrSpecInnerServiceSMOImpl.deleteAttrSpec(attrSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
