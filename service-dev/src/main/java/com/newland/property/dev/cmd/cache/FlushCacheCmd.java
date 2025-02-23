package com.newland.property.dev.cmd.cache;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dev.smo.IDevServiceCacheSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 保存编码映射处理类
 */
@NewlandPropertyCmd(serviceCode = "flush.center.cache")
public class FlushCacheCmd extends Cmd {

    @Autowired
    IDevServiceCacheSMO devServiceCacheSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        devServiceCacheSMOImpl.flush(reqJson.toJavaObject(Map.class));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
