package com.newland.property.store.cmd.property;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.service.context.DataQuery;
import com.newland.property.service.smo.IQueryServiceSMO;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "check.property.staffHasProperty")
public class CheckPropertyStaffHasPropertyCmd extends Cmd {
    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");
        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.user.privilege");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return ;
        }
        String resObj = responseEntity.getBody().toString();

        Assert.isJsonObject(resObj,"下游服务返回格式错误，不是有效json格式"+resObj);

        JSONObject resJson = JSONObject.parseObject(resObj);

        Assert.jsonObjectHaveKey(resJson,"count","下游服务返回格式错误，返回报文中未包含count"+resObj);

        long count = resJson.getLongValue("count");

        if(count < 1){
            throw  new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"当前员工没有相关物业信息，数据异常请检查");
        }

        responseEntity = new ResponseEntity<String>("成功",HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
