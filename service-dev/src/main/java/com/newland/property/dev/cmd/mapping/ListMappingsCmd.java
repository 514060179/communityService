package com.newland.property.dev.cmd.mapping;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.mapping.MappingDto;
import com.newland.property.intf.community.IMappingInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.mapping.ApiMappingDataVo;
import com.newland.property.vo.api.mapping.ApiMappingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "mapping.listMappings")
public class ListMappingsCmd extends Cmd {

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.queryMappingsCount(mappingDto);

        List<ApiMappingDataVo> mappings = null;

        if (count > 0) {
            mappings = BeanConvertUtil.covertBeanList(mappingInnerServiceSMOImpl.queryMappings(mappingDto), ApiMappingDataVo.class);
        } else {
            mappings = new ArrayList<>();
        }

        ApiMappingVo apiMappingVo = new ApiMappingVo();

        apiMappingVo.setTotal(count);
        apiMappingVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMappingVo.setMappings(mappings);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMappingVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
