package com.newland.property.dev.cmd.menu;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.menu.MenuDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.menu.ApiMenuDataVo;
import com.newland.property.vo.api.menu.ApiMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "menu.listMenus")
public class ListMenusCmd extends Cmd {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MenuDto menuDto = BeanConvertUtil.covertBean(reqJson, MenuDto.class);

        int count = menuInnerServiceSMOImpl.queryMenusCount(menuDto);

        List<ApiMenuDataVo> menus = null;

        if (count > 0) {
            menus = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryMenus(menuDto), ApiMenuDataVo.class);
        } else {
            menus = new ArrayList<>();
        }

        ApiMenuVo apiMenuVo = new ApiMenuVo();

        apiMenuVo.setTotal(count);
        apiMenuVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMenuVo.setMenus(menus);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMenuVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
