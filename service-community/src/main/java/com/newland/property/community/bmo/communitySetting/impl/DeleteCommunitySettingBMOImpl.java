package com.newland.property.community.bmo.communitySetting.impl;

import com.newland.property.community.bmo.communitySetting.IDeleteCommunitySettingBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.CommunitySettingFactory;
import com.newland.property.intf.community.ICommunitySettingInnerServiceSMO;
import com.newland.property.po.community.CommunitySettingPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteCommunitySettingBMOImpl")
public class DeleteCommunitySettingBMOImpl implements IDeleteCommunitySettingBMO {

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    /**
     * @param communitySettingPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(CommunitySettingPo communitySettingPo) {

        int flag = communitySettingInnerServiceSMOImpl.deleteCommunitySetting(communitySettingPo);
        //将结果写入缓存
        CommunitySettingFactory.deleteCommunitySetting(communitySettingPo);
        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
