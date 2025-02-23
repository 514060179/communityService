package com.newland.property.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.org.OrgCommunityDto;
import com.newland.property.intf.user.IOrgCommunityInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.org.ApiOrgCommunityDataVo;
import com.newland.property.vo.api.org.ApiOrgCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@NewlandPropertyCmd(serviceCode = "org.listOrgCommunitys")
public class ListOrgCommunitysCmd extends Cmd {

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "orgId", "必填，请填写组织ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OrgCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, OrgCommunityDto.class);

        int count = orgCommunityInnerServiceSMOImpl.queryOrgCommunitysCount(orgCommunityDto);

        List<ApiOrgCommunityDataVo> orgCommunitys = null;

        if (count > 0) {
            orgCommunitys = BeanConvertUtil.covertBeanList(orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto), ApiOrgCommunityDataVo.class);
        } else {
            orgCommunitys = new ArrayList<>();
        }

        ApiOrgCommunityVo apiOrgVo = new ApiOrgCommunityVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgCommunitys(orgCommunitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
