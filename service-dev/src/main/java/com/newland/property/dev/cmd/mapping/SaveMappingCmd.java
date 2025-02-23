package com.newland.property.dev.cmd.mapping;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.mapping.MappingDto;
import com.newland.property.intf.community.IMappingInnerServiceSMO;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NewlandPropertyCmd(serviceCode = "mapping.saveMapping")
public class SaveMappingCmd extends Cmd {
    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "domain", "必填，请填写域");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写名称");
        Assert.hasKeyAndValue(reqJson, "key", "必填，请填写键");
        Assert.hasKeyAndValue(reqJson, "value", "必填，请填写值");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.saveMapping(mappingDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
