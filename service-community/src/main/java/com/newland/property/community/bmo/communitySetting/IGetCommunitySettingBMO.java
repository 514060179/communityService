package com.newland.property.community.bmo.communitySetting;
import com.newland.property.dto.community.CommunitySettingDto;
import org.springframework.http.ResponseEntity;
public interface IGetCommunitySettingBMO {


    /**
     * 查询小区相关设置
     * add by wuxw
     * @param  communitySettingDto
     * @return
     */
    ResponseEntity<String> get(CommunitySettingDto communitySettingDto);


}
