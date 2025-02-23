package com.newland.property.api.bmo.org.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.org.IOrgBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.user.IOrgInnerServiceSMO;
import com.newland.property.dto.org.OrgDto;
import com.newland.property.po.community.OrgCommunityPo;
import com.newland.property.po.org.OrgPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OrgBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:10
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("orgBMOImpl")
public class OrgBMOImpl extends ApiBaseBMO implements IOrgBMO {
    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteOrgCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        OrgCommunityPo orgCommunityPo = BeanConvertUtil.covertBean(paramInJson, OrgCommunityPo.class);
        super.delete(dataFlowContext, orgCommunityPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ORG_COMMUNITY);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteOrg(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OrgPo orgPo = BeanConvertUtil.covertBean(paramInJson, OrgPo.class);
        super.delete(dataFlowContext, orgPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ORG);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addOrgCommunity(JSONObject paramInJson, JSONObject communityObj, int seq, DataFlowContext dataFlowContext) {

        JSONObject businessOrg = new JSONObject();
        businessOrg.putAll(paramInJson);
        businessOrg.put("orgCommunityId", "-1");
        businessOrg.put("communityId", communityObj.getString("communityId"));
        businessOrg.put("communityName", communityObj.getString("communityName"));

        OrgCommunityPo orgCommunityPo = BeanConvertUtil.covertBean(businessOrg, OrgCommunityPo.class);
        super.insert(dataFlowContext, orgCommunityPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_COMMUNITY);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addOrg(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOrg = new JSONObject();
        businessOrg.putAll(paramInJson);
        businessOrg.put("orgId", "-1");
        businessOrg.put("allowOperation", "T");
        businessOrg.put("belongCommunityId", "");

        OrgPo orgPo = BeanConvertUtil.covertBean(businessOrg, OrgPo.class);
        super.insert(dataFlowContext, orgPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG);

    }


    /**
     * 添加组织管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateOrg(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OrgDto orgDto = new OrgDto();
        orgDto.setOrgId(paramInJson.getString("orgId"));
        orgDto.setStoreId(paramInJson.getString("storeId"));
        List<OrgDto> orgDtos = orgInnerServiceSMOImpl.queryOrgs(orgDto);

        Assert.listOnlyOne(orgDtos, "未查询到组织信息 或查询到多条数据");

        JSONObject businessOrg = new JSONObject();
        businessOrg.putAll(paramInJson);
        businessOrg.put("allowOperation", orgDtos.get(0).getAllowOperation());
        businessOrg.put("belongCommunityId", orgDtos.get(0).getBelongCommunityId());
        OrgPo orgPo = BeanConvertUtil.covertBean(businessOrg, OrgPo.class);
        super.insert(dataFlowContext, orgPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ORG);

    }
}
