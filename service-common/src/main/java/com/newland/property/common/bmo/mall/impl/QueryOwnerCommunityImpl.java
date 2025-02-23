package com.newland.property.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.mall.IMallCommonApiBmo;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("queryOwnerCommunityImpl")
public class QueryOwnerCommunityImpl implements IMallCommonApiBmo {

    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {


        String link = reqJson.getString("link");

        String ownerId = reqJson.getString("ownerId");

        if (StringUtil.isEmpty(link) && StringUtil.isEmpty(ownerId)) {
            throw new IllegalArgumentException("未包含手机号和业主ID");
        }

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setLink(reqJson.getString("link"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }


        //todo 查询小区

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        if (ListUtil.isNull(communityDtos)) {
            throw new CmdException("小区不存在");
        }


        //todo 查询物业信息
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityDtos.get(0).getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        if (ListUtil.isNull(communityMemberDtos)) {
            throw new CmdException("物业不存在");
        }


        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        if (ListUtil.isNull(storeDtos)) {
            throw new CmdException("物业不存在");
        }

        JSONObject data = new JSONObject();
        data.put("ownerId", ownerDtos.get(0).getMemberId());
        data.put("ownerName", ownerDtos.get(0).getName());
        data.put("ownerTel", ownerDtos.get(0).getLink());
        data.put("communityId", communityDtos.get(0).getCommunityId());
        data.put("communityName", communityDtos.get(0).getName());
        data.put("storeId", storeDtos.get(0).getStoreId());
        data.put("storeName", storeDtos.get(0).getName());

        context.setResponseEntity(ResultVo.createResponseEntity(data));


    }
}
