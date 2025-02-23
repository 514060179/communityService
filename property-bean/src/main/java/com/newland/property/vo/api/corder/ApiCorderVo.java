package com.newland.property.vo.api.corder;


import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCorderVo extends MorePageVo implements Serializable{
    List<ApiCorderDataVo> corderDataVos;

    public List<ApiCorderDataVo> getCorderDataVos() {
        return corderDataVos;
    }

    public void setCorderDataVos(List<ApiCorderDataVo> corderDataVos) {
        this.corderDataVos = corderDataVos;
    }
}
