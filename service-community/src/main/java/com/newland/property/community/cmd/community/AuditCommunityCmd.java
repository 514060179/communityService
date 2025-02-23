package com.newland.property.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.po.community.CommunityPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "community.auditCommunity")
public class AuditCommunityCmd extends Cmd {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写小区审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写小区审核原因");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "未查询到该小区信息【" + communityDto.getCommunityId() + "】");
        communityDto = communityDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityDto);
        oldCommunityInfo.put("state", reqJson.getString("state"));
        CommunityPo communityPo = BeanConvertUtil.covertBean(oldCommunityInfo, CommunityPo.class);
        int flag = communityV1InnerServiceSMOImpl.updateCommunity(communityPo);

        if (flag < 1) {
            throw new CmdException("更新小区失败");
        }
    }
}
