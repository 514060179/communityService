package com.newland.property.vo.api.org;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiOrgCommunityVo extends MorePageVo implements Serializable {
    List<ApiOrgCommunityDataVo> orgCommunitys;


    public List<ApiOrgCommunityDataVo> getOrgCommunitys() {
        return orgCommunitys;
    }

    public void setOrgCommunitys(List<ApiOrgCommunityDataVo> orgCommunitys) {
        this.orgCommunitys = orgCommunitys;
    }
}
