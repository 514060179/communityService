package com.newland.property.user.bmo.owner.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IOwnerAppUserInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.user.bmo.owner.IChangeOwnerPhone;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChangeOwnerPhoneImpl implements IChangeOwnerPhone {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;



    @Override
    @PropertyTransactional
    public ResponseEntity<String> change(OwnerPo ownerPo) {

        String userId = ownerPo.getUserId();
        ownerPo.setUserId("");
        //修改业主手机号
        ownerInnerServiceSMOImpl.updateOwnerMember(ownerPo);

        UserPo userPo = new UserPo();
        userPo.setUserId(userId);
        userPo.setTel(ownerPo.getLink());
        userInnerServiceSMOImpl.updateUser(userPo);

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setMemberId(ownerPo.getMemberId());
        ownerAppUserPo.setCommunityId(ownerPo.getCommunityId());
        ownerAppUserPo.setLink(ownerPo.getLink());
        ownerAppUserInnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);

        return ResultVo.success();
    }


}
