package com.newland.property.dev.cmd.app;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.app.AppDto;
import com.newland.property.intf.community.IAppInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.app.ApiAppDataVo;
import com.newland.property.vo.api.app.ApiAppVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@NewlandPropertyCmd(serviceCode = "app.listApps")
public class ListAppsCmd extends Cmd {


    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AppDto appDto = BeanConvertUtil.covertBean(reqJson, AppDto.class);

        int count = appInnerServiceSMOImpl.queryAppsCount(appDto);

        List<ApiAppDataVo> apps = null;

        if (count > 0) {
            apps = BeanConvertUtil.covertBeanList(appInnerServiceSMOImpl.queryApps(appDto), ApiAppDataVo.class);
        } else {
            apps = new ArrayList<>();
        }

        ApiAppVo apiAppVo = new ApiAppVo();

        apiAppVo.setTotal(count);
        apiAppVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAppVo.setApps(apps);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAppVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
