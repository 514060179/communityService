package com.newland.property.vo.api.menuGroup;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMenuGroupVo extends MorePageVo implements Serializable {
    List<ApiMenuGroupDataVo> menuGroups;


    public List<ApiMenuGroupDataVo> getMenuGroups() {
        return menuGroups;
    }

    public void setMenuGroups(List<ApiMenuGroupDataVo> menuGroups) {
        this.menuGroups = menuGroups;
    }
}
