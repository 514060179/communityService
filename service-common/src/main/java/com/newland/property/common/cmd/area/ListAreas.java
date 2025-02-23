package com.newland.property.common.cmd.area;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.area.AreaDto;
import com.newland.property.intf.common.IAreaInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.area.ApiAreaDataVo;
import com.newland.property.vo.api.area.ApiAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "area.listAreas")
public class ListAreas extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AreaDto areaDto = BeanConvertUtil.covertBean(reqJson, AreaDto.class);

        List<ApiAreaDataVo> areas = BeanConvertUtil.covertBeanList(areaInnerServiceSMOImpl.getArea(areaDto),ApiAreaDataVo.class);


        ApiAreaVo apiAreaVo = new ApiAreaVo();

        apiAreaVo.setTotal(1);
        apiAreaVo.setRecords(1);
        apiAreaVo.setAreas(areas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAreaVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
