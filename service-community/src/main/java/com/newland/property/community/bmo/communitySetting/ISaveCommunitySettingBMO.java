package com.newland.property.community.bmo.communitySetting;

import com.newland.property.po.community.CommunitySettingPo;
import org.springframework.http.ResponseEntity;
public interface ISaveCommunitySettingBMO {


    /**
     * 添加小区相关设置
     * add by wuxw
     * @param communitySettingPo
     * @return
     */
    ResponseEntity<String> save(CommunitySettingPo communitySettingPo);


}
