package com.newland.property.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.CommunitySettingFactory;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.community.IRepairInnerServiceSMO;
import com.newland.property.intf.community.IRepairUserInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "ownerRepair.listStaffFinishRepairs")
public class ListStaffFinishRepairsCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    public static final String VIEW_LIST_STAFF_REPAIRS = "VIEW_LIST_STAFF_REPAIRS";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "userId", "请求中未包含员工信息");
    }

    /**
     * 报修已办
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);
        String userId = reqJson.getString("userId");
        String viewListStaffRepairs = CommunitySettingFactory.getValue(reqJson.getString("communityId"), VIEW_LIST_STAFF_REPAIRS);
        if (StringUtil.isEmpty(viewListStaffRepairs)) {
            viewListStaffRepairs = MappingCache.getValue(MappingConstant.REPAIR_DOMAIN,VIEW_LIST_STAFF_REPAIRS);
        }
        List<Map> privileges = null;
        //这里加开关 其实让管理员看到所有单子这么做，不太优雅，建议 单独开发页面功能
        // 不要影响已办功能，add by 吴学文 2021-09-09
        if ("ON".equals(viewListStaffRepairs)) {//是否让管理员看到所有工单
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/listStaffFinishRepairs");
            basePrivilegeDto.setUserId(userId);
            privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        }
        if (privileges == null || privileges.size() == 0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }
        if (reqJson.containsKey("repairStates")) {
            String[] states = reqJson.getString("repairStates").split(",");
            ownerRepairDto.setStates(Arrays.asList(states));
        } else {
            //Pc WEB维修已办
            String[] states={RepairDto.STATE_BACK, RepairDto.STATE_TRANSFER,RepairDto.STATE_PAY, RepairDto.STATE_PAY_ERROR, RepairDto.STATE_APPRAISE, RepairDto.STATE_RETURN_VISIT, RepairDto.STATE_COMPLATE};
            ownerRepairDto.setStates(Arrays.asList(states));
        }
        int count = repairInnerServiceSMOImpl.queryStaffFinishRepairsCount(ownerRepairDto);
        List<RepairDto> ownerRepairs = null;
        if (count > 0) {
            ownerRepairs = repairInnerServiceSMOImpl.queryStaffFinishRepairs(ownerRepairDto);
            //refreshStaffName(ownerRepairs);
        } else {
            ownerRepairs = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerRepairs);
        context.setResponseEntity(responseEntity);
    }
}
