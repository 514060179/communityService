package com.newland.property.common.bmo.attrValue;

import com.newland.property.po.attrSpec.AttrValuePo;
import org.springframework.http.ResponseEntity;

public interface ISaveAttrValueBMO {


    /**
     * 添加属性值
     * add by wuxw
     *
     * @param attrValuePo
     * @return
     */
    ResponseEntity<String> save(AttrValuePo attrValuePo);


}
