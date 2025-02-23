package com.newland.property.vo.api.fastuser;

import com.newland.property.vo.MorePageVo;
import com.newland.property.vo.api.activities.ApiActivitiesDataVo;

import java.io.Serializable;
import java.util.List;

public class ApiFastuserVo extends MorePageVo implements Serializable {

    List<ApiFastuserDataVo> fastuserDataVos;

    public List<ApiFastuserDataVo> getFastuserDataVos() {
        return fastuserDataVos;
    }

    public void setFastuserDataVos(List<ApiFastuserDataVo> fastuserDataVos) {
        this.fastuserDataVos = fastuserDataVos;
    }

}
