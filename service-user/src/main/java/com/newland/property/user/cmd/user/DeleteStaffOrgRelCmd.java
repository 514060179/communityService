package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.org.OrgStaffRelDto;
import com.newland.property.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.newland.property.po.org.OrgStaffRelPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "org.deleteOrgRelStaff")
public class DeleteStaffOrgRelCmd extends Cmd {
    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "orgId", "未包含组织信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(reqJson.getString("userId"));
        //组织关系数
        List<OrgStaffRelDto> orgStaffRelDtos1 = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        if (orgStaffRelDtos1.size() < 2) {
            throw new CmdException("至少保留一个组织关系，暂时无法删除！");
        }
        orgStaffRelDto.setRelId(reqJson.getString("relId"));
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        if (orgStaffRelDtos == null || orgStaffRelDtos.size() < 1) {
            throw new CmdException("关系不存在");
        }
        for (OrgStaffRelDto tmpOrgStaffRelDto : orgStaffRelDtos) {
            OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
            orgStaffRelPo.setRelId(tmpOrgStaffRelDto.getRelId());
            int flag = orgStaffRelV1InnerServiceSMOImpl.deleteOrgStaffRel(orgStaffRelPo);
            if (flag < 1) {
                throw new CmdException("关联员工失败");
            }
        }
    }
}
