package com.newland.property.user.bmo.owner;

import com.newland.property.po.owner.OwnerPo;

public interface IGeneratorOwnerUserBMO {

    /**
     * 业主生成登录账号
     * @param ownerPo
     */
     void generator(OwnerPo ownerPo);

    void connectLoginUser(OwnerPo ownerPo);
}
