package com.newland.property.common.cmd.corders;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.corder.CorderDto;
import com.newland.property.intf.order.ICordersInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 受理订单查询
 */
@NewlandPropertyCmd(serviceCode = "corders.listCorders")
public class ListCordersCmd extends Cmd {

    @Autowired
    private ICordersInnerServiceSMO cordersInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CorderDto corderDto = BeanConvertUtil.covertBean(reqJson, CorderDto.class);

        int count = cordersInnerServiceSMOImpl.queryCordersCount(corderDto);

        List<CorderDto> corderVos = null;

        if (count > 0) {
            corderVos = cordersInnerServiceSMOImpl.queryCorders(corderDto);
        } else {
            corderVos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),count,corderVos);

        context.setResponseEntity(responseEntity);
    }
}
