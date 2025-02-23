package com.newland.property.vo.api.app;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAppVo extends MorePageVo implements Serializable {
    List<ApiAppDataVo> apps;


    public List<ApiAppDataVo> getApps() {
        return apps;
    }

    public void setApps(List<ApiAppDataVo> apps) {
        this.apps = apps;
    }
}
