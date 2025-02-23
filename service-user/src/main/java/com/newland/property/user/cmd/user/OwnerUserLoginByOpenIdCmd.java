package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.dto.system.SystemInfoDto;
import com.newland.property.dto.user.LoginOwnerResDto;
import com.newland.property.dto.user.UserAttrDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.ISmsInnerServiceSMO;
import com.newland.property.intf.common.ISystemInfoV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.user.*;
import com.newland.property.po.user.UserAttrPo;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 业主登录，专门提供业主 使用
 * 微信openId 登录
 */
@NewlandPropertyCmd(serviceCode = "user.ownerUserLoginByOpenId")
public class OwnerUserLoginByOpenIdCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(UserLoginCmd.class);

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;


    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private ISystemInfoV1InnerServiceSMO systemInfoV1InnerServiceSMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "openId", "请求报文中未包含openId");

        //todo openId 转换
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        userAttrDto.setValue(reqJson.getString("openId"));
        List<UserAttrDto> userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);

        if (ListUtil.isNull(userAttrDtos)) {
            throw new CmdException("未找到用户信息");
        }

        // todo  2.0 校验 业主用户绑定表是否存在记录
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userAttrDtos.get(0).getUserId());
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        String communityId = "";
        if (!ListUtil.isNull(ownerAppUserDtos)) {
            // todo 4.0 查询小区是否存在
            communityId = ownerAppUserDtos.get(0).getCommunityId();
        } else {
            SystemInfoDto systemInfoDto = new SystemInfoDto();
            List<SystemInfoDto> systemInfoDtos = systemInfoV1InnerServiceSMOImpl.querySystemInfos(systemInfoDto);
            communityId = systemInfoDtos.get(0).getDefaultCommunityId();
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "小区不存在，确保开发者账户配置默认小区" + communityId);


        UserDto userDto = new UserDto();
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);
        userDto.setUserId(userAttrDtos.get(0).getUserId());

        //todo 1.0 查询用户是否存在
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (userDtos == null || userDtos.size() < 1) {
            throw new CmdException("业主不存在，请先注册");
        }

        UserDto tmpUserDto = userDtos.get(0);
        String newKey = generatorLoginKey(tmpUserDto);

        //todo 生成登录token
        String token = generatorLoginToken(tmpUserDto);
        LoginOwnerResDto loginOwnerResDto = new LoginOwnerResDto();

        loginOwnerResDto.setCommunityId(communityDtos.get(0).getCommunityId());
        loginOwnerResDto.setCommunityName(communityDtos.get(0).getName());
        loginOwnerResDto.setCommunityTel(communityDtos.get(0).getTel());
        loginOwnerResDto.setUserId(tmpUserDto.getUserId());
        loginOwnerResDto.setUserName(tmpUserDto.getName());
        loginOwnerResDto.setOwnerTel(tmpUserDto.getTel());
        loginOwnerResDto.setOwnerAreaCode(tmpUserDto.getAreaCode());
        loginOwnerResDto.setToken(token);
        loginOwnerResDto.setKey(newKey);
        context.setResponseEntity(ResultVo.createResponseEntity(loginOwnerResDto));
    }

    /**
     * 生成登录key
     *
     * @param tmpUserDto
     * @return
     */
    private static String generatorLoginToken(UserDto tmpUserDto) {
        String token;
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, tmpUserDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, tmpUserDto.getUserName());
            token = AuthenticationFactory.createAndSaveToken(userMap);
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new CmdException("系统内部错误，请联系管理员");
        }
        return token;
    }

    private String generatorLoginKey(UserDto tmpUserDto) {
        List<UserAttrDto> userAttrDtos = tmpUserDto.getUserAttrs();
        UserAttrDto userAttrDto = getCurrentUserAttrDto(userAttrDtos, UserAttrDto.SPEC_KEY);
        String newKey = UUID.randomUUID().toString();
        if (userAttrDto != null) {
            UserAttrPo userAttrPo = BeanConvertUtil.covertBean(userAttrDto, UserAttrPo.class);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
        } else {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            userAttrPo.setUserId(tmpUserDto.getUserId());
            userAttrPo.setSpecCd(UserAttrDto.SPEC_KEY);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
        }
        return newKey;
    }

    private UserAttrDto getCurrentUserAttrDto(List<UserAttrDto> userAttrDtos, String specCd) {
        if (userAttrDtos == null) {
            return null;
        }
        for (UserAttrDto userAttrDto : userAttrDtos) {
            if (specCd.equals(userAttrDto.getSpecCd())) {
                return userAttrDto;
            }
        }

        return null;
    }
}
