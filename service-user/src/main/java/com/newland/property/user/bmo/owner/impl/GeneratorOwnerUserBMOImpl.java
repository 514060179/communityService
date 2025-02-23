package com.newland.property.user.bmo.owner.impl;

import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.UserLevelConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneratorOwnerUserBMOImpl implements IGeneratorOwnerUserBMO {


    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void generator(OwnerPo ownerPo) {
        String autoUser = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH, "AUTO_GENERATOR_OWNER_USER");

        if (!"ON".equals(autoUser)) {
            return;
        }

        int flag = 0;

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        UserDto userDto = new UserDto();
        userDto.setTel(ownerPo.getLink());
        userDto.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        String userId = "";
        UserPo userPo = new UserPo();
        userPo.setName(ownerPo.getName());
        userPo.setTel(ownerPo.getLink());
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        userPo.setAge(ownerPo.getAge());
        userPo.setAddress(ownerPo.getAddress());
        userPo.setSex(ownerPo.getSex());
        userPo.setAreaCode(ownerPo.getAreaCode());
        if (ListUtil.isNull(userDtos)) {
            userPo.setPassword(AuthenticationFactory.passwdMd5(ownerPo.getLink()));
            userPo.setUserId(GenerateCodeFactory.getUserId());
            flag = userV1InnerServiceSMOImpl.saveUser(userPo);
            userId = userPo.getUserId();
        } else {
            //修改登录信息
            userId = userDtos.get(0).getUserId();
            userPo.setUserId(userId);
            flag = userV1InnerServiceSMOImpl.updateUser(userPo);
        }
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setState("12000");
        ownerAppUserPo.setAppTypeCd("10010");
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId(ownerPo.getMemberId());
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(ownerPo.getCommunityId());
        ownerAppUserPo.setAppUserName(ownerPo.getName());
        ownerAppUserPo.setIdCard(ownerPo.getIdCard());
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(ownerPo.getLink());
        ownerAppUserPo.setUserId(userId);
        ownerAppUserPo.setOpenId("-1");
        ownerAppUserPo.setAreaCode(ownerPo.getAreaCode());
        ownerAppUserPo.setOwnerTypeCd(ownerPo.getOwnerTypeCd());

        flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("添加用户业主关系失败");
        }
    }

    @Override
    public void connectLoginUser(OwnerPo ownerPo) {
        String autoUser = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH, "AUTO_GENERATOR_OWNER_USER");

        if (!"ON".equals(autoUser)) {
            return;
        }

        int flag = 0;

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        UserDto userDto = new UserDto();
        userDto.setTel(ownerPo.getLink());
        userDto.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        String userId = "";
        UserPo userPo = new UserPo();
        userPo.setName(ownerPo.getName());
        userPo.setTel(ownerPo.getLink());
        userPo.setPassword(AuthenticationFactory.passwdMd5(ownerPo.getLink()));
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        userPo.setAge(ownerPo.getAge());
        userPo.setAddress(ownerPo.getAddress());
        userPo.setSex(ownerPo.getSex());
        userPo.setAreaCode(ownerPo.getAreaCode());
        if (ListUtil.isNull(userDtos)) {
            return;
        }

        userId = userDtos.get(0).getUserId();

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setState("12000");
        ownerAppUserPo.setAppTypeCd("10010");
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId(ownerPo.getMemberId());
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(ownerPo.getCommunityId());
        ownerAppUserPo.setAppUserName(ownerPo.getName());
        ownerAppUserPo.setIdCard(ownerPo.getIdCard());
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(ownerPo.getLink());
        ownerAppUserPo.setUserId(userId);
        ownerAppUserPo.setOpenId("-1");
        ownerAppUserPo.setAreaCode(ownerPo.getAreaCode());
        ownerAppUserPo.setOwnerTypeCd(ownerPo.getOwnerTypeCd());

        flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("添加用户业主关系失败");
        }
    }
}
