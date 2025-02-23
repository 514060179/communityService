package com.newland.property.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.org.OrgDto;
import com.newland.property.intf.user.IOrgInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.org.ApiOrgDataVo;
import com.newland.property.vo.api.org.ApiOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "org.listParentOrgs")
public class ListParentOrgsCmd extends Cmd {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);



        List<ApiOrgDataVo> parentOrgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryParentOrgs(orgDto), ApiOrgDataVo.class);


        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setOrgs(parentOrgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
