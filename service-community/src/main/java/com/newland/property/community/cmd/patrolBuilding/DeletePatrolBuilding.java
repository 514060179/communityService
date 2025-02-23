package com.newland.property.community.cmd.patrolBuilding;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IPatrolBuildingV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.patrolBuilding.PatrolBuildingPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "patrolBuilding.deletePatrolBuilding")
public class DeletePatrolBuilding extends Cmd {

    @Autowired
    private IPatrolBuildingV1InnerServiceSMO iPatrolBuildingV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "pbId", "请求报文中未包含巡楼ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        JSONObject businessPatrolBuilding = new JSONObject();
        businessPatrolBuilding.put("pbId", reqJson.getString("pbId"));
        PatrolBuildingPo patrolBuildingPo = BeanConvertUtil.covertBean(businessPatrolBuilding, PatrolBuildingPo.class);
        int flag = iPatrolBuildingV1InnerServiceSMOImpl.deletePatrolBuilding(patrolBuildingPo);
        if (flag < 1) {
            throw new CmdException("删除巡楼信息失败");
        }
        //删除图片信息
        FileRelDto fileDto = new FileRelDto();
        fileDto.setObjId(reqJson.getString("pbId"));
        List<FileRelDto> fileRelList = fileRelInnerServiceSMOImpl.queryFileRels(fileDto);
        if (fileRelList != null && fileRelList.size() > 0) {
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setObjId(reqJson.getString("pbId"));
            fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
        }
    }
}
