package com.newland.property.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.org.OrgDto;
import com.newland.property.dto.org.OrgStaffRelDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.user.IOrgInnerServiceSMO;
import com.newland.property.intf.user.IOrgStaffRelInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.org.ApiOrgDataVo;
import com.newland.property.vo.api.org.ApiOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "org.listOrgs")
public class ListOrgsCmd extends Cmd {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
//        String storeId = context.getReqHeaders().get("store-id");
//        reqJson.put("storeId", storeId);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);
        //2级别组织信息
        if (reqJson.containsKey("orgLevel") && "2".equals(reqJson.getString("orgLevel"))) {
            //默认只查看当前归属组织架构
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllOrganization");
            basePrivilegeDto.setUserId(reqJson.getString("userId"));
            List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
            if (privileges.size() == 0) {
                //查询员工所属二级组织架构
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                orgStaffRelDto.setStaffId(reqJson.getString("userId"));
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIds(orgStaffRelDto);
                if (orgStaffRelDtos.size() > 0) {
                    orgDto.setOrgId(orgStaffRelDtos.get(0).getCompanyId());//当前人虽归属的二级组织信息
                }

            }
        }

        int count = orgInnerServiceSMOImpl.queryOrgsCount(orgDto);

        List<ApiOrgDataVo> orgs = null;

        if (count > 0) {
            orgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryOrgs(orgDto), ApiOrgDataVo.class);
        } else {
            orgs = new ArrayList<>();
        }

        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgs(orgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
