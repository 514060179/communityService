package com.newland.property.dev.cmd.menuGroup;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.menu.MenuGroupDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.menuGroup.ApiMenuGroupDataVo;
import com.newland.property.vo.api.menuGroup.ApiMenuGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "menuGroup.listMenuGroups")
public class ListMenuGroupsCmd extends Cmd{

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);

        int count = menuInnerServiceSMOImpl.queryMenuGroupsCount(menuGroupDto);

        List<ApiMenuGroupDataVo> menuGroups = null;

        if (count > 0) {
            menuGroups = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryMenuGroups(menuGroupDto), ApiMenuGroupDataVo.class);
        } else {
            menuGroups = new ArrayList<>();
        }

        ApiMenuGroupVo apiMenuGroupVo = new ApiMenuGroupVo();

        apiMenuGroupVo.setTotal(count);
        apiMenuGroupVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMenuGroupVo.setMenuGroups(menuGroups);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMenuGroupVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
