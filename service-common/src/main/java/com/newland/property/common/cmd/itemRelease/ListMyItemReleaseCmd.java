package com.newland.property.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.itemRelease.ItemReleaseDto;
import com.newland.property.intf.common.IItemReleaseResV1InnerServiceSMO;
import com.newland.property.intf.common.IItemReleaseV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@NewlandPropertyCmd(serviceCode = "itemRelease.listMyItemRelease")
public class ListMyItemReleaseCmd extends Cmd {

    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Autowired
    private IItemReleaseResV1InnerServiceSMO itemReleaseResV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        ItemReleaseDto itemReleaseDto = BeanConvertUtil.covertBean(reqJson, ItemReleaseDto.class);
        itemReleaseDto.setCreateUserId(userId);

        int count = itemReleaseV1InnerServiceSMOImpl.queryItemReleasesCount(itemReleaseDto);

        List<ItemReleaseDto> itemReleaseDtos = null;

        if (count > 0) {
            itemReleaseDtos = itemReleaseV1InnerServiceSMOImpl.queryItemReleases(itemReleaseDto);

        } else {
            itemReleaseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, itemReleaseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
