package com.newland.property.community.cmd.index;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.dto.audit.AuditUser;
import com.newland.property.intf.common.IComplaintUserInnerServiceSMO;
import com.newland.property.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.newland.property.intf.common.IResourceEntryStoreInnerServiceSMO;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IRepairInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "index.queryIndexTodoTask")
public class QueryIndexTodoTaskCmd extends Cmd {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IResourceEntryStoreInnerServiceSMO resourceEntryStoreInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        AuditUser auditUser = new AuditUser();
        auditUser.setStoreId(reqJson.getString("storeId"));
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setCommunityId(reqJson.getString("communityId"));
        //投诉待办
        long complaintCount = complaintUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        //投诉已办
        long complaintHisCount = complaintUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);


        //报修 待办
        RepairDto ownerRepairDto = new RepairDto();
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewListStaffRepairs");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size()==0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }
        ownerRepairDto.setCommunityId(reqJson.getString("communityId"));
        int repairCount = repairInnerServiceSMOImpl.queryStaffRepairsCount(ownerRepairDto);

        //报修已办
        basePrivilegeDto.setResource("/listStaffFinishRepairs");
        List<Map> privileges2 = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges2.size()==0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }else{
            ownerRepairDto.setStaffId("");
        }
        int repairHisCount = repairInnerServiceSMOImpl.queryStaffFinishRepairsCount(ownerRepairDto);

        // 采购待办

        long purchaseCount = resourceEntryStoreInnerServiceSMOImpl.getUserTaskCount(auditUser);

        //采购已办
        long purchaseHisCount = resourceEntryStoreInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        //物品领用待办

        long collectionCount = goodCollectionUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        long collectionHisCount = goodCollectionUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);


        JSONObject paramOut = new JSONObject();
        paramOut.put("complaintCount", complaintCount);
        paramOut.put("complaintHisCount", complaintHisCount);
        paramOut.put("repairCount", repairCount);
        paramOut.put("repairHisCount", repairHisCount);
        paramOut.put("purchaseCount", purchaseCount);
        paramOut.put("purchaseHisCount", purchaseHisCount);
        paramOut.put("collectionCount", collectionCount);
        paramOut.put("collectionHisCount", collectionHisCount);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
