package com.newland.property.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.api.community.ApiCommunityDataVo;
import com.newland.property.vo.api.community.ApiCommunityVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询 入驻审核小区
 * add by 吴学文 2021-09-18
 */
@NewlandPropertyCmd(serviceCode = "community.listAuditEnterCommunitys")
public class ListAuditEnterCommunitysCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(ListAuditEnterCommunitysCmd.class);


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        if (StringUtil.jsonHasKayAndValue(reqJson, "name") || StringUtil.jsonHasKayAndValue(reqJson, "tel")) {
            getInfoByStore(cmdDataFlowContext, reqJson);
        } else {
            getInfoByCommunity(cmdDataFlowContext, reqJson);
        }
    }

    private void getInfoByStore(ICmdDataFlowContext context, JSONObject reqJson) {
        StoreDto storeDto = new StoreDto();
        storeDto.setName(reqJson.getString("name"));
        storeDto.setTel(reqJson.getString("tel"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

        List<ApiCommunityDataVo> communitys = null;
        int count = 0;
        if (storeDtos.size() > 0) {
            CommunityMemberDto communityMemberDto = BeanConvertUtil.covertBean(reqJson, CommunityMemberDto.class);
            communityMemberDto.setNeedCommunityInfo(true);
            communityMemberDto.setNoAuditEnterCommunity(true);
            communityMemberDto.setMemberId(reqJson.getString("memberId"));
            communityMemberDto.setSubMemberId(storeDtos.get(0).getStoreId());

            count = communityInnerServiceSMOImpl.getCommunityMemberCount(communityMemberDto);

            if (count > 0) {
                communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto), ApiCommunityDataVo.class);
            } else {
                communitys = new ArrayList<>();
            }
        } else {
            communitys = new ArrayList<>();
        }

        //刷入 商户信息
        if (communitys.size() > 0) {
            freshCommunityInfo(communitys);
        }

        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 小区 商户信息
     *
     * @param communityDataVos
     */
    private void freshCommunityInfo(List<ApiCommunityDataVo> communityDataVos) {
        for (ApiCommunityDataVo apiCommunityDataVo : communityDataVos) {
            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(apiCommunityDataVo.getMemberId());
            List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

            if (storeDtos.size() != 1) {
                continue;
            }
            //BeanConvertUtil.covertBean(storeDtos.get(0), apiCommunityDataVo);

            apiCommunityDataVo.setStoreName(storeDtos.get(0).getStoreName());
            apiCommunityDataVo.setStoreTypeCd(storeDtos.get(0).getStoreTypeCd());
            apiCommunityDataVo.setStoreTypeName(storeDtos.get(0).getStoreTypeName());
            apiCommunityDataVo.setTel(storeDtos.get(0).getTel());
            apiCommunityDataVo.setAddress(storeDtos.get(0).getAddress());
        }
    }

    private void getInfoByCommunity(ICmdDataFlowContext context, JSONObject reqJson) {
        CommunityMemberDto communityMemberDto = BeanConvertUtil.covertBean(reqJson, CommunityMemberDto.class);

        communityMemberDto.setNeedCommunityInfo(true);
        communityMemberDto.setNoAuditEnterCommunity(true);

        int count = communityInnerServiceSMOImpl.getCommunityMemberCount(communityMemberDto);

        List<ApiCommunityDataVo> communitys = null;

        if (count > 0) {
            communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto), ApiCommunityDataVo.class);
        } else {
            communitys = new ArrayList<>();
        }

        //刷入 商户信息
        if (communitys.size() > 0) {
            freshCommunityInfo(communitys);
        }

        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
