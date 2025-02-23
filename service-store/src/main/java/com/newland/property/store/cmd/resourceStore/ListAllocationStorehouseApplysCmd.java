package com.newland.property.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.purchase.AllocationStorehouseApplyDto;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
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

/**
 * 查询调拨记录
 */
@NewlandPropertyCmd(serviceCode = "resourceStore.listAllocationStorehouseApplys")
public class ListAllocationStorehouseApplysCmd extends Cmd {


    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseApplyDto.class);
        //调拨记录（调拨记录所有权限查看所有数据）
        privilegeControl(context, allocationStorehouseApplyDto);
        int count = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplysCount(allocationStorehouseApplyDto);

        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = null;

        if (count > 0) {
            allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        } else {
            allocationStorehouseApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationStorehouseApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 权限控制
     *
     * @param context
     * @param allocationStorehouseApplyDto
     */
    private void privilegeControl(ICmdDataFlowContext context, AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        if (!StringUtil.isEmpty(allocationStorehouseApplyDto.getApplyId())) { //单个信息查询时不做控制
            return;
        }
        String userId = CmdContextUtils.getUserId(context);
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewlistAllocationStorehouses");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            allocationStorehouseApplyDto.setStartUserId(userId);
        }
    }
}
