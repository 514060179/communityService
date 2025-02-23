package com.newland.property.vo.api.user;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiUserVo extends MorePageVo implements Serializable {
    List<ApiUserDataVo> users;

    public List<ApiUserDataVo> getUsers() {
        return users;
    }

    public void setUsers(List<ApiUserDataVo> users) {
        this.users = users;
    }
}
