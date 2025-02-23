package com.newland.property.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.purchase.AllocationStorehouseDto;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "resourceStore.listAllocationStorehouses")
public class ListAllocationStorehousesCmd extends Cmd {

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);
        AllocationStorehouseDto allocationStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseDto.class);
        allocationStorehouseDto.setStoreId(storeId);
        //获取用户id
        String userId = reqJson.getString("userId");
        //调拨记录（调拨记录所有权限查看所有数据）
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewlistAllocationStorehouses");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            allocationStorehouseDto.setStartUserId(userId);
        }
        //调拨记录详情、调拨待办查看、调拨已办查看
        if (!StringUtil.isEmpty(reqJson.getString("applyOrderId"))) {
            allocationStorehouseDto.setAsId(reqJson.getString("applyOrderId"));
            allocationStorehouseDto.setStartUserId("");
        }

        int count = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehousesCount(allocationStorehouseDto);

        List<AllocationStorehouseDto> allocationStorehouseDtos = null;

        if (count > 0) {
            allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        } else {
            allocationStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
