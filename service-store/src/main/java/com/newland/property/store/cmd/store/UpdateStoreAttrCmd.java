package com.newland.property.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.store.StoreAttrDto;
import com.newland.property.intf.store.IStoreAttrInnerServiceSMO;
import com.newland.property.intf.store.IStoreAttrV1InnerServiceSMO;
import com.newland.property.po.store.StoreAttrPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "storeAttr.updateStoreAttr")
public class UpdateStoreAttrCmd extends Cmd {

    @Autowired
    private IStoreAttrInnerServiceSMO storeAttrInnerServiceSMOImpl;

    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        StoreAttrDto storeAttrDto = new StoreAttrDto();
        storeAttrDto.setAttrId(reqJson.getString("attrId"));
        List<StoreAttrDto> storeAttrDtos = storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);

        Assert.listOnlyOne(storeAttrDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessStoreAttr = new JSONObject();
        businessStoreAttr.putAll(BeanConvertUtil.beanCovertMap(storeAttrDtos.get(0)));
        businessStoreAttr.putAll(reqJson);

        StoreAttrPo storeAttrPo = BeanConvertUtil.covertBean(businessStoreAttr, StoreAttrPo.class);

        int flag = storeAttrV1InnerServiceSMOImpl.updateStoreAttr(storeAttrPo);

        if (flag < 1) {
            throw new CmdException("修改异常");
        }
    }
}
