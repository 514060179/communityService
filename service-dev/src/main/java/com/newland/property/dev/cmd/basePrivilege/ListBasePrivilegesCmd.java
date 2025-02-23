package com.newland.property.dev.cmd.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.basePrivilege.ApiBasePrivilegeDataVo;
import com.newland.property.vo.api.basePrivilege.ApiBasePrivilegeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "basePrivilege.listBasePrivileges")
public class ListBasePrivilegesCmd extends Cmd{

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(reqJson, BasePrivilegeDto.class);

        int count = menuInnerServiceSMOImpl.queryBasePrivilegesCount(basePrivilegeDto);

        List<ApiBasePrivilegeDataVo> basePrivileges = null;

        if (count > 0) {
            basePrivileges = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryBasePrivileges(basePrivilegeDto), ApiBasePrivilegeDataVo.class);
        } else {
            basePrivileges = new ArrayList<>();
        }

        ApiBasePrivilegeVo apiBasePrivilegeVo = new ApiBasePrivilegeVo();

        apiBasePrivilegeVo.setTotal(count);
        apiBasePrivilegeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiBasePrivilegeVo.setBasePrivileges(basePrivileges);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiBasePrivilegeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
