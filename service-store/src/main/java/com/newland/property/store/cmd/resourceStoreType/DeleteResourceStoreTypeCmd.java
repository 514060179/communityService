package com.newland.property.store.cmd.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.resource.ResourceStoreTypeDto;
import com.newland.property.intf.community.IResourceStoreServiceSMO;
import com.newland.property.intf.store.IResourceStoreTypeV1InnerServiceSMO;
import com.newland.property.po.purchase.ResourceStorePo;
import com.newland.property.po.resource.ResourceStoreTypePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "resourceStoreType.deleteResourceStoreType")
public class DeleteResourceStoreTypeCmd extends Cmd {

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Autowired
    private IResourceStoreTypeV1InnerServiceSMO resourceStoreTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "rstId", "rstId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setRstId(reqJson.getString("rstId"));
        if (!StringUtil.isEmpty(reqJson.getString("parentId")) && "0".equals(reqJson.getString("parentId"))) {
            //根据类型查询是否有子分类
            ResourceStoreTypeDto resourceStoreTypeDto = new ResourceStoreTypeDto();
            resourceStoreTypeDto.setParentId(reqJson.getString("rstId"));
            List<ResourceStoreTypeDto> resourceStores = resourceStoreTypeV1InnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
            Assert.listIsNull(resourceStores, "该物品类型下有二级分类，不能删除！");
        } else {
            //根据类型id物品信息表
            List<ResourceStorePo> resourceStores = resourceStoreServiceSMOImpl.getResourceStores(resourceStorePo);
            Assert.listIsNull(resourceStores, "物品信息中该类型正在使用，不能删除！");
        }

        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypePo.class);

        int flag = resourceStoreTypeV1InnerServiceSMOImpl.deleteResourceStoreType(resourceStoreTypePo);

        if (flag < 1) {
            throw new CmdException("删除失败");
        }
    }
}
