package com.newland.property.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.po.store.StorePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "update.store.info")
public class UpdateStoreInfoCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
        Assert.listOnlyOne(storeDtos, "未找到需要修改的商户或多条数据");

        JSONObject businessStore = new JSONObject();
        businessStore.putAll(BeanConvertUtil.beanCovertMap(storeDtos.get(0)));
        businessStore.putAll(reqJson);
        StorePo storePo = BeanConvertUtil.covertBean(businessStore, StorePo.class);
        int flag = storeV1InnerServiceSMOImpl.updateStore(storePo);
        if (flag < 1) {
            throw new CmdException("修改商户失败");
        }
    }
}
