package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.service.context.DataQuery;
import com.newland.property.service.smo.IQueryServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "query.user.privilege")
public class QueryUserPrivilege extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        //String storeId = context.getReqHeaders().get("store-id");

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }

        Assert.hasLength(userId, "未包含用户");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }

        if(reqJson.containsKey("staffId") && !StringUtil.isEmpty(reqJson.getString("staffId"))){
            userId = reqJson.getString("staffId");
        }

        String domain = "";
        if(!reqJson.containsKey("domain") || StringUtil.isEmpty(reqJson.getString("domain"))) {

            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeId);
            storeDto.setPage(1);
            storeDto.setRow(1);
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

            Assert.listOnlyOne(storeDtos, "商户不存在");
            domain = storeDtos.get(0).getStoreTypeCd();
        }else{
            domain = reqJson.getString("domain");
        }

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.user.privilege");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        param.put("domain", domain);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> privilegeGroup = dataQuery.getResponseEntity();
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(privilegeGroup);
            return ;
        }
        JSONObject resultObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        JSONArray privileges = resultObj.getJSONArray("privileges");

        JSONArray tmpPrivilegeArrays = new JSONArray();

        JSONObject privilegeObj = null;
        for (int privilegeIndex = 0; privilegeIndex < privileges.size(); privilegeIndex++) {
            privilegeObj = privileges.getJSONObject(privilegeIndex);
            hasSameData(privilegeObj, tmpPrivilegeArrays);
        }

        JSONObject resObj = new JSONObject();
        resObj.put("datas", privileges);
        context.setResponseEntity(new ResponseEntity<String>(resObj.toJSONString(), HttpStatus.OK));
    }

    private void hasSameData(JSONObject privilegeObj, JSONArray tmpPrivilegeArrays) {
        JSONObject tmpPrivilegeObj = null;
        for (int tmpPrivilegeIndex = 0; tmpPrivilegeIndex < tmpPrivilegeArrays.size(); tmpPrivilegeIndex++) {
            tmpPrivilegeObj = tmpPrivilegeArrays.getJSONObject(tmpPrivilegeIndex);
            if (privilegeObj.getString("pId").equals(tmpPrivilegeObj.getString("pId"))) {
                if (!StringUtil.isEmpty(privilegeObj.getString("pgId"))) {
                    tmpPrivilegeArrays.remove(tmpPrivilegeIndex);
                    tmpPrivilegeArrays.add(privilegeObj);
                }
                return;
            }
        }
        tmpPrivilegeArrays.add(privilegeObj);
    }
}
