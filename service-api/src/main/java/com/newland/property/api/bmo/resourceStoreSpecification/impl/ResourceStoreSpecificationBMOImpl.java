package com.newland.property.api.bmo.resourceStoreSpecification.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.resourceStoreSpecification.IResourceStoreSpecificationBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.dto.resource.ResourceStoreDto;
import com.newland.property.intf.store.IResourceStoreInnerServiceSMO;
import com.newland.property.intf.store.IResourceStoreSpecificationInnerServiceSMO;
import com.newland.property.po.resource.ResourceStoreSpecificationPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("resourceStoreSpecificationBMOImpl")
public class ResourceStoreSpecificationBMOImpl extends ApiBaseBMO implements IResourceStoreSpecificationBMO {

    @Autowired
    private IResourceStoreSpecificationInnerServiceSMO resourceStoreSpecificationInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("rssId", "-1");
        ResourceStoreSpecificationPo resourceStoreSpecificationPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreSpecificationPo.class);
        super.insert(dataFlowContext, resourceStoreSpecificationPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_SPECIFICATION);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceStoreSpecificationPo resourceStoreSpecificationPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreSpecificationPo.class);
        super.update(dataFlowContext, resourceStoreSpecificationPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE_SPECIFICATION);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setRssId(paramInJson.getString("rssId"));
        //查询物品信息表
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        Assert.listIsNull(resourceStoreDtos, "物品信息中该规格正在使用，不能删除！");
        ResourceStoreSpecificationPo resourceStoreSpecificationPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreSpecificationPo.class);
        super.update(dataFlowContext, resourceStoreSpecificationPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE_SPECIFICATION);
    }

}
