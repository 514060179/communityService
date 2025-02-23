package com.newland.property.dev.cmd.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.service.ServiceBusinessDto;
import com.newland.property.intf.community.IServiceBusinessInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.serviceImpl.ApiServiceImplDataVo;
import com.newland.property.vo.api.serviceImpl.ApiServiceImplVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "serviceImpl.listServiceImpls")
public class ListServiceImplsCmd extends Cmd {

    @Autowired
    private IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ServiceBusinessDto serviceImplDto = BeanConvertUtil.covertBean(reqJson, ServiceBusinessDto.class);

        int count = serviceBusinessInnerServiceSMOImpl.queryServiceBusinesssCount(serviceImplDto);

        List<ApiServiceImplDataVo> serviceImpls = null;

        if (count > 0) {
            serviceImpls = BeanConvertUtil.covertBeanList(serviceBusinessInnerServiceSMOImpl.queryServiceBusinesss(serviceImplDto), ApiServiceImplDataVo.class);
        } else {
            serviceImpls = new ArrayList<>();
        }

        ApiServiceImplVo apiServiceImplVo = new ApiServiceImplVo();

        apiServiceImplVo.setTotal(count);
        apiServiceImplVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceImplVo.setServiceImpls(serviceImpls);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceImplVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
