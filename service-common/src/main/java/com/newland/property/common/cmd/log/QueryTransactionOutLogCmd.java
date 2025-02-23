package com.newland.property.common.cmd.log;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.log.TransactionOutLogDto;
import com.newland.property.intf.common.ITransactionOutLogV1ServiceSMO;
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

@NewlandPropertyCmd(serviceCode = "log.queryTransactionOutLog")
public class QueryTransactionOutLogCmd extends Cmd {

    @Autowired
    private ITransactionOutLogV1ServiceSMO transactionOutLogV1ServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"logType","未包含日志类型");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        TransactionOutLogDto transactionOutLogDto = BeanConvertUtil.covertBean(reqJson, TransactionOutLogDto.class);

        int count = transactionOutLogV1ServiceSMOImpl.queryTransactionOutLogsCount(transactionOutLogDto);

        List<TransactionOutLogDto> transactionOutLogDtos = null;

        if (count > 0) {
            transactionOutLogDtos = transactionOutLogV1ServiceSMOImpl.queryTransactionOutLogs(transactionOutLogDto);
        } else {
            transactionOutLogDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, transactionOutLogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
