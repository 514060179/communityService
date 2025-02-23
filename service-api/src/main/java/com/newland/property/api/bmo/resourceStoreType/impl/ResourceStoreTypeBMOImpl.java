package com.newland.property.api.bmo.resourceStoreType.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.dto.resource.ResourceStoreTypeDto;
import com.newland.property.intf.community.IResourceStoreServiceSMO;
import com.newland.property.api.bmo.resourceStoreType.IResourceStoreTypeBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IResourceStoreTypeInnerServiceSMO;
import com.newland.property.po.purchase.ResourceStorePo;
import com.newland.property.po.resource.ResourceStoreTypePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("resourceStoreTypeBMOImpl")
public class ResourceStoreTypeBMOImpl extends ApiBaseBMO implements IResourceStoreTypeBMO {

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Autowired
    private IResourceStoreTypeInnerServiceSMO resourceStoreTypeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("rstId", "-1");
        paramInJson.put("createTime", new Date());
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreTypePo.class);
        super.insert(dataFlowContext, resourceStoreTypePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_TYPE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        /*ResourceStoreTypeDto resourceStoreTypeDto = new ResourceStoreTypeDto();
        resourceStoreTypeDto.setGoodsType(paramInJson.getString("goodsType"));
        resourceStoreTypeDto.setStatusCd("0");
        List<ResourceStoreTypeDto> resourceStoreTypeDtos = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
        if (resourceStoreTypeDtos != null && resourceStoreTypeDtos.size() > 0) {
            for (ResourceStoreTypeDto resourceStoreType : resourceStoreTypeDtos) {
                if (!resourceStoreType.getRstId().equals(paramInJson.getString("rstId"))) {
                    throw new IllegalArgumentException("物品类型编码已存在，请重新输入！");
                }
            }
        }*/
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreTypePo.class);
        super.update(dataFlowContext, resourceStoreTypePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE_TYPE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        if (paramInJson.containsKey("parentId") && "0".equals(paramInJson.getString("parentId"))) {
            ResourceStoreTypeDto resourceStoreTypeDto = new ResourceStoreTypeDto();
            resourceStoreTypeDto.setParentId(paramInJson.getString("rstId"));
            List<ResourceStoreTypeDto> resourceStoreTypeDtos = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
            Assert.listIsNull(resourceStoreTypeDtos, "请先删除二级分类！");
        } else if (paramInJson.containsKey("parentId") && !"0".equals(paramInJson.getString("parentId"))) {
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setRstId(paramInJson.getString("rstId"));
            //根据类型id物品信息表
            List<ResourceStorePo> resourceStores = resourceStoreServiceSMOImpl.getResourceStores(resourceStorePo);
            Assert.listIsNull(resourceStores, "物品信息中该类型正在使用，不能删除！");
        }
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreTypePo.class);
        super.update(dataFlowContext, resourceStoreTypePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE_TYPE);
    }

}
