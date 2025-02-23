package com.newland.property.dev.cmd.menuGroup;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.menu.MenuGroupDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NewlandPropertyCmd(serviceCode = "menuGroup.saveMenuGroup")
public class SaveMenuGroupCmd extends Cmd{

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写组名称");
        Assert.hasKeyAndValue(reqJson, "icon", "必填，请填写icon");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;

        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);

        freshGId(menuGroupDto);


        int saveFlag = menuInnerServiceSMOImpl.saveMenuGroup(menuGroupDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 菜单组ID
     * @param menuGroupDto
     */
    private void freshGId(MenuGroupDto menuGroupDto) {

        if(!StringUtils.isEmpty(menuGroupDto.getgId())){
            return ;
        }
        //生成流水
        menuGroupDto.setgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.MENU_GROUP));
    }
}
