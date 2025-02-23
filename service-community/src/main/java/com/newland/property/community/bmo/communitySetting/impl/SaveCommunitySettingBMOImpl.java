package com.newland.property.community.bmo.communitySetting.impl;

import com.newland.property.community.bmo.communitySetting.ISaveCommunitySettingBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.CommunitySettingFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.ICommunitySettingInnerServiceSMO;
import com.newland.property.po.community.CommunitySettingPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("saveCommunitySettingBMOImpl")
public class SaveCommunitySettingBMOImpl implements ISaveCommunitySettingBMO {

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param communitySettingPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(CommunitySettingPo communitySettingPo) {

        communitySettingPo.setCsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_csId));
        int flag = communitySettingInnerServiceSMOImpl.saveCommunitySetting(communitySettingPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //将结果写入缓存
        CommunitySettingFactory.getCommunitySettingFromDb(communitySettingPo.getCommunityId(), communitySettingPo.getSettingKey());


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
