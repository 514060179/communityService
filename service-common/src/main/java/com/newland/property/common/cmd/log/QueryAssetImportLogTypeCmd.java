package com.newland.property.common.cmd.log;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.log.AssetImportLogTypeDto;
import com.newland.property.intf.common.IAssetImportLogInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询导入类型 字段
 */
@NewlandPropertyCmd(serviceCode = "log.queryAssetImportLogType")
public class QueryAssetImportLogTypeCmd extends Cmd {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "logType", "未包含类型");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        AssetImportLogTypeDto assetImportLogTypeDto = BeanConvertUtil.covertBean(reqJson, AssetImportLogTypeDto.class);
        List<AssetImportLogTypeDto> assetImportLogTypes = assetImportLogInnerServiceSMOImpl.queryAssetImportLogType(assetImportLogTypeDto);

        context.setResponseEntity(ResultVo.createResponseEntity(assetImportLogTypes));
    }
}
