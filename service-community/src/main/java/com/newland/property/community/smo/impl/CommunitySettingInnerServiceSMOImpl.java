package com.newland.property.community.smo.impl;


import com.newland.property.community.dao.ICommunitySettingServiceDao;
import com.newland.property.dto.community.CommunitySettingDto;
import com.newland.property.intf.community.ICommunitySettingInnerServiceSMO;
import com.newland.property.po.community.CommunitySettingPo;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 小区相关设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommunitySettingInnerServiceSMOImpl extends BaseServiceSMO implements ICommunitySettingInnerServiceSMO {

    @Autowired
    private ICommunitySettingServiceDao communitySettingServiceDaoImpl;


    @Override
    public int saveCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo) {
        int saveFlag = 1;
        communitySettingServiceDaoImpl.saveCommunitySettingInfo(BeanConvertUtil.beanCovertMap(communitySettingPo));
        return saveFlag;
    }

    @Override
    public int updateCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo) {
        int saveFlag = 1;
        communitySettingServiceDaoImpl.updateCommunitySettingInfo(BeanConvertUtil.beanCovertMap(communitySettingPo));
        return saveFlag;
    }

    @Override
    public int deleteCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo) {
        int saveFlag = 1;
        communitySettingPo.setStatusCd("1");
        communitySettingServiceDaoImpl.updateCommunitySettingInfo(BeanConvertUtil.beanCovertMap(communitySettingPo));
        return saveFlag;
    }

    @Override
    public List<CommunitySettingDto> queryCommunitySettings(@RequestBody CommunitySettingDto communitySettingDto) {

        //校验是否传了 分页信息

        int page = communitySettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communitySettingDto.setPage((page - 1) * communitySettingDto.getRow());
        }

        List<CommunitySettingDto> communitySettings = BeanConvertUtil.covertBeanList(communitySettingServiceDaoImpl.getCommunitySettingInfo(BeanConvertUtil.beanCovertMap(communitySettingDto)), CommunitySettingDto.class);

        return communitySettings;
    }


    @Override
    public int queryCommunitySettingsCount(@RequestBody CommunitySettingDto communitySettingDto) {
        return communitySettingServiceDaoImpl.queryCommunitySettingsCount(BeanConvertUtil.beanCovertMap(communitySettingDto));
    }

    public ICommunitySettingServiceDao getCommunitySettingServiceDaoImpl() {
        return communitySettingServiceDaoImpl;
    }

    public void setCommunitySettingServiceDaoImpl(ICommunitySettingServiceDao communitySettingServiceDaoImpl) {
        this.communitySettingServiceDaoImpl = communitySettingServiceDaoImpl;
    }
}
