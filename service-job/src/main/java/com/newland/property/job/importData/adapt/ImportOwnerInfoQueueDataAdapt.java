package com.newland.property.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.log.AssetImportLogDetailDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.job.importData.DefaultImportData;
import com.newland.property.job.importData.IImportDataAdapt;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.po.user.UserPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.UserLevelConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Moonny
 */
@Service("importOwnerInfoQueueData")
public class ImportOwnerInfoQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        importDatas(assetImportLogDetailDtos);
    }

    private void importDatas(List<AssetImportLogDetailDto> infos) {
        for (AssetImportLogDetailDto assetImportLogDetailDto : infos) {
            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    /**
     * 导入数据
     *
     * @param assetImportLogDetailDto
     */
    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        OwnerPo ownerPo = BeanConvertUtil.covertBean(data, OwnerPo.class);
        String link = ownerPo.getLink();
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setAreaCode(ownerPo.getAreaCode());
        ownerDto.setCommunityId(ownerPo.getCommunityId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (!ownerDtos.isEmpty()) {
            throw new IllegalArgumentException("手机号重复");
        } else {
            if (!ownerPo.getOwnerTypeCd().equals(OwnerDto.OWNER_TYPE_CD_OWNER)) {
                ownerDto = new OwnerDto();
                ownerDto.setLink(data.getString("ownerLink"));
                ownerDto.setAreaCode(data.getString("ownerAreaCode"));
                ownerDto.setCommunityId(ownerPo.getCommunityId());
                List<OwnerDto> owners = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
                if (!owners.isEmpty()) {
                    ownerPo.setOwnerId(owners.get(0).getMemberId());
                } else {
                    throw new IllegalArgumentException("业主信息为录入: " + data.getString("ownerAreaCode") + "-" + data.getString("ownerLink"));
                }
            }

            int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
            if (flag < 1) {
                throw new IllegalArgumentException("导入失败");
            }

            // todo 生成登录账号
            this.generator(ownerPo);
        }
    }

    private void connectLoginUser(OwnerPo ownerPo) {
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

    private void generator(OwnerPo ownerPo) {
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
            if (!userPo.getTel().equals(userDtos.get(0).getTel())) {
                userPo.setPassword(AuthenticationFactory.passwdMd5(ownerPo.getLink()));
            }

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

        // todo 同步业主信息到iot
        JSONObject reqJson = new JSONObject();
        reqJson.put("memberId", ownerPo.getMemberId());
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_TO_IOT, reqJson));
    }
}
