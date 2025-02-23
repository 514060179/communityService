package com.newland.property.dev.cmd.business;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.system.CbusinesstypeDto;
import com.newland.property.intf.store.ICbusinesstypeInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "business.queryBusinessTypeConfig")
public class QueryBusinessTypeConfigCmd extends Cmd {

    @Autowired
    private ICbusinesstypeInnerServiceSMO iCbusinesstypeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        //validateDemoConfigData(reqJson);

        List<CbusinesstypeDto> cbusinesstypeDto = iCbusinesstypeInnerServiceSMOImpl.queryCbusinesstypes(BeanConvertUtil.covertBean(reqJson, CbusinesstypeDto.class));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(cbusinesstypeDto), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
