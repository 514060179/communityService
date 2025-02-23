package com.newland.property.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.resource.ResourceStoreTimesDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询价格物品
 * add by wuxw 2023-01-19
 */
@NewlandPropertyCmd(serviceCode = "resourceStore.listResourceStoresAndTimes")
public class ListResourceStoresAndTimesCmd extends Cmd {

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStoreTimesDto resourceStoreTimesDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTimesDto.class);

        int count = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimessCount(resourceStoreTimesDto);
        List<ResourceStoreTimesDto> resourceStores = null;
        if (count > 0) {
            resourceStores = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
        } else {
            resourceStores = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStores);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
