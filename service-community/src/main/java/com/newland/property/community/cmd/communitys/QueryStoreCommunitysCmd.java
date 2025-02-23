package com.newland.property.community.cmd.communitys;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询商户小区
 * add by 吴学文 2021-09-18
 */
@NewlandPropertyCmd(serviceCode = "/communitys/queryStoreCommunitys")
public class QueryStoreCommunitysCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    private final static Logger logger = LoggerFactory.getLogger(QueryStoreCommunitysCmd.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        if(StringUtil.isEmpty(storeId)){
            storeId = reqJson.getString("memberId");
        }
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setPage(1);
        storeDto.setRow(1);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");
        CommunityMemberDto communityMemberDto = BeanConvertUtil.covertBean(reqJson, CommunityMemberDto.class);
        communityMemberDto.setMemberId(storeId);
        communityMemberDto.setMemberTypeCd(MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeDtos.get(0).getStoreTypeCd()));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.getStoreCommunitys(communityMemberDto);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(communityDtos));
    }
}
