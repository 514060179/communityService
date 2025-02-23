package com.newland.property.user.bmo.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.po.owner.OwnerPo;
import org.springframework.http.ResponseEntity;

public interface IChangeOwnerPhone {

    /**
     * 修改业主手机号
     * @param ownerPo
     * @return
     */
    ResponseEntity<String> change(OwnerPo ownerPo);
}
