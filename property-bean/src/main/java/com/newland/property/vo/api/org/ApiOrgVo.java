package com.newland.property.vo.api.org;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiOrgVo extends MorePageVo implements Serializable {
    List<ApiOrgDataVo> orgs;


    public List<ApiOrgDataVo> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<ApiOrgDataVo> orgs) {
        this.orgs = orgs;
    }
}
