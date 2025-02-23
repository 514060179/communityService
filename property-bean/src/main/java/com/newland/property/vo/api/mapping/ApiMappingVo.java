package com.newland.property.vo.api.mapping;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMappingVo extends MorePageVo implements Serializable {
    List<ApiMappingDataVo> mappings;


    public List<ApiMappingDataVo> getMappings() {
        return mappings;
    }

    public void setMappings(List<ApiMappingDataVo> mappings) {
        this.mappings = mappings;
    }
}
