package com.newland.property.common.bmo.attrSpec;

import com.newland.property.dto.attrSpec.AttrSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetAttrSpecBMO {


    /**
     * 查询属性规格表
     * add by wuxw
     *
     * @param attrSpecDto
     * @return
     */
    ResponseEntity<String> get(AttrSpecDto attrSpecDto);


}
