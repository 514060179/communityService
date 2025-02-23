package com.newland.property.vo.api.basePrivilege;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiBasePrivilegeVo extends MorePageVo implements Serializable {
    List<ApiBasePrivilegeDataVo> basePrivileges;


    public List<ApiBasePrivilegeDataVo> getBasePrivileges() {
        return basePrivileges;
    }

    public void setBasePrivileges(List<ApiBasePrivilegeDataVo> basePrivileges) {
        this.basePrivileges = basePrivileges;
    }
}
