package com.newland.property.store.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.shop.ShopDto;
import com.newland.property.dto.shop.StoreShopDto;
import com.newland.property.intf.store.IStoreShopV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 申请商户的临时票据
 */
@NewlandPropertyCmd(serviceCode = "shop.applyShopCode")
public class ApplyShopCodeCmd extends Cmd {

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "shopId", "未包含商铺信息");

        String storeId = context.getReqHeaders().get("store-id");

        StoreShopDto storeShopDto = new StoreShopDto();
        storeShopDto.setShopId(reqJson.getString("shopId"));
        storeShopDto.setStoreId(storeId);
        List<ShopDto> storeShopDtos = storeShopV1InnerServiceSMOImpl.queryStoreShops(storeShopDto);

        Assert.listOnlyOne(storeShopDtos, "您没有权限操作");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String code = GenerateCodeFactory.getUUID();

        CommonCache.setValue(reqJson.getString("shopId")+code,code,CommonCache.PAY_DEFAULT_EXPIRE_TIME);

        context.setResponseEntity(ResultVo.createResponseEntity(code));

    }
}
