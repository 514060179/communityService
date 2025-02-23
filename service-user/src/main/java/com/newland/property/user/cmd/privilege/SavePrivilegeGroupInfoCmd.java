package com.newland.property.user.cmd.privilege;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.privilege.PrivilegeGroupDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.order.IPrivilegeInnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.IPrivilegeGroupV1InnerServiceSMO;
import com.newland.property.po.privilege.PrivilegeGroupPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "save.privilegeGroup.info")
public class SavePrivilegeGroupInfoCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeGroupV1InnerServiceSMO privilegeGroupV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name节点");
        String storeId = context.getReqHeaders().get("store-id");
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setPage(1);
        storeDto.setRow(1);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        Assert.listOnlyOne(storeDtos, "商户不存在");
        reqJson.put("storeId", storeDtos.get(0).getStoreId());
        reqJson.put("storeTypeCd", storeDtos.get(0).getStoreTypeCd());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        reqJson.put("pgId", GenerateCodeFactory.getPgId());
        PrivilegeGroupPo privilegeGroupPo = BeanConvertUtil.covertBean(reqJson, PrivilegeGroupPo.class);
        privilegeGroupPo.setDomain(reqJson.getString("storeTypeCd"));
        privilegeGroupPo.setRoleType(PrivilegeGroupDto.ROLE_TYPE_USER);
        int flag = privilegeGroupV1InnerServiceSMOImpl.savePrivilegeGroup(privilegeGroupPo);
        if (flag < 1) {
            throw new CmdException("保存失败");
        }

    }
}
