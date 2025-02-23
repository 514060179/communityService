package com.newland.property.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.service.context.DataQuery;
import com.newland.property.service.smo.IQueryServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 查询用户是否存在 商户信息
 */
@NewlandPropertyCmd(serviceCode = "query.store.byuser")
public class QueryStoreByUserCmd extends Cmd {
    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = event.getCmdDataFlowContext().getReqHeaders().get("user-id");

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.store.byuser");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();
        //todo 直接查询失败，所以直接返回异常
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(new ResponseEntity<>("初始化商户", HttpStatus.FORBIDDEN));
            return;
        }


        //todo 说明是员工 返回查询信息
        String storeInfo = responseEntity.getBody();
        if (Assert.isJsonObject(storeInfo) && JSONObject.parseObject(storeInfo).containsKey("storeId")) {
            context.setResponseEntity(responseEntity);
            return;
        }
        //todo 说明是业主 返回空即可
        context.setResponseEntity(ResultVo.success());
    }
}
