package com.newland.property.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.oaWorkflow.OaWorkflowDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.visit.VisitDto;
import com.newland.property.dto.visit.VisitSettingDto;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.dto.audit.AuditUser;
import com.newland.property.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.newland.property.intf.community.IVisitSettingV1InnerServiceSMO;
import com.newland.property.intf.community.IVisitV1InnerServiceSMO;
import com.newland.property.intf.oa.IOaWorkflowInnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询物品放行已办单
 */
@NewlandPropertyCmd(serviceCode = "visit.queryFinishVisit")
public class QueryFinishVisitCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setState(OaWorkflowDto.STATE_COMPLAINT);
        oaWorkflowDto.setFlowType(OaWorkflowDto.FLOW_TYPE_VISIT);
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        if (oaWorkflowDtos == null || oaWorkflowDtos.size() < 1) {
            return;
        }
        List<String> flowIds = new ArrayList<>();
        for (OaWorkflowDto tmpOaWorkflowDto : oaWorkflowDtos) {
            flowIds.add(WorkflowDto.DEFAULT_PROCESS + tmpOaWorkflowDto.getFlowId());
        }

        AuditUser auditUser = new AuditUser();
        auditUser.setProcessDefinitionKeys(flowIds);
        auditUser.setUserId(userId);
        auditUser.setStoreId(storeId);
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));

        long count = oaWorkflowUserInnerServiceSMOImpl.getDefinitionKeysUserHistoryTaskCount(auditUser);

        List<JSONObject> datas = null;

        if (count > 0) {
            datas = oaWorkflowUserInnerServiceSMOImpl.getDefinitionKeysUserHistoryTasks(auditUser);
            //刷新 表单数据
            refreshFormData(datas, reqJson);

            // 输入flowId
            refreshSetting(datas, reqJson);

            //刷新 业主
            refreshOwners(datas, reqJson);

        } else {
            datas = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    private void refreshOwners(List<JSONObject> datas, JSONObject reqJson) {

        if (datas == null || datas.size() < 1) {
            return;
        }

        List<String> ownerIds = new ArrayList<>();
        for (JSONObject apiVisitDataVo : datas) {
            ownerIds.add(apiVisitDataVo.getString("ownerId"));
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        for (JSONObject apiVisitDataVo : datas) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (!apiVisitDataVo.getString("ownerId").equals(tmpOwnerDto.getOwnerId())) {
                    continue;
                }
                apiVisitDataVo.put("ownerName", tmpOwnerDto.getName());
                apiVisitDataVo.put("ownerTel", tmpOwnerDto.getLink());
            }
        }

    }

    private void refreshFormData(List<JSONObject> datas, JSONObject paramIn) {

        List<String> ids = new ArrayList<>();
        for (JSONObject data : datas) {
            ids.add(data.getString("id"));
        }
        if (ids.size() < 1) {
            return;
        }

        VisitDto visitDto = new VisitDto();
        visitDto.setvIds(ids.toArray(new String[ids.size()]));
        List<VisitDto> itemReleaseDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
        if (itemReleaseDtos == null || itemReleaseDtos.size() < 1) {
            return;
        }
        for (JSONObject data : datas) {
            for (VisitDto form : itemReleaseDtos) {
                if (data.getString("id").equals(form.getvId())) {
                    data.putAll(BeanConvertUtil.beanCovertJson(form));
                }
            }
        }
    }


    private void refreshSetting(List<JSONObject> datas, JSONObject reqJson) {
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);

        if (visitSettingDtos == null || visitSettingDtos.size() < 1) {
            return;
        }

        if (datas == null || datas.size() < 1) {
            return;
        }

        for (JSONObject data : datas) {
            data.put("flowId", visitSettingDtos.get(0).getFlowId());
        }
    }
}
