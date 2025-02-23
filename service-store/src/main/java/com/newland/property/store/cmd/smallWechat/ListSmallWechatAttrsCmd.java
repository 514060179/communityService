package com.newland.property.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.wechat.SmallWechatAttrDto;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "smallWechat.listSmallWechatAttrs")
public class ListSmallWechatAttrsCmd extends Cmd {

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "wechatId", "微信ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        SmallWechatAttrDto smallWechatAttrDto = BeanConvertUtil.covertBean(reqJson, SmallWechatAttrDto.class);

        int count = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrsCount(smallWechatAttrDto);

        List<SmallWechatAttrDto> smallWechatAttrDtos = null;

        if (count > 0) {
            smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        } else {
            smallWechatAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, smallWechatAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
