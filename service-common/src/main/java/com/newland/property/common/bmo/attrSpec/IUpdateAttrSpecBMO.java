package com.newland.property.common.bmo.attrSpec;

import com.newland.property.po.attrSpec.AttrSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttrSpecBMO {


    /**
     * 修改属性规格表
     * add by wuxw
     *
     * @param attrSpecPo
     * @return
     */
    ResponseEntity<String> update(AttrSpecPo attrSpecPo);


}
