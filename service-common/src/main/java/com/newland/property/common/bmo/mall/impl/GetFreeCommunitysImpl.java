package com.newland.property.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.mall.IMallCommonApiBmo;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getFreeCommunitysImpl")
public class GetFreeCommunitysImpl implements IMallCommonApiBmo {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "areaCode", "未包含地区");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        JSONArray notInCommunityId = reqJson.getJSONArray("notInCommunityId");

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCityCode(reqJson.getString("areaCode"));
        if (!ListUtil.isNull(notInCommunityId)) {
            communityDto.setNotInCommunityId(notInCommunityId.toArray(new String[notInCommunityId.size()]));
        }

        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        context.setResponseEntity(ResultVo.createResponseEntity(communityDtos));

    }
}
