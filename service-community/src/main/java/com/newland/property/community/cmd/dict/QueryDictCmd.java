package com.newland.property.community.cmd.dict;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.dict.DictDto;
import com.newland.property.dto.dict.DictQueryDto;
import com.newland.property.intf.community.DictInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存编码映射处理类
 */
@NewlandPropertyCmd(serviceCode = "dict.queryDict")
public class QueryDictCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(QueryDictCmd.class);

    @Autowired
    private DictInnerServiceSMO dictInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        List<DictDto> dictDtos = this.dictInnerServiceSMO.queryDict(BeanConvertUtil.covertBean(reqJson, DictQueryDto.class));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(dictDtos), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
