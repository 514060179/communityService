package com.newland.property.common.cmd.file;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "file.saveFile")
public class SaveFileCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "fileName", "必填，请填写文件名称");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写文件内容");
        Assert.hasKeyAndValue(reqJson, "suffix", "必填，请填写文件扩展");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FileDto fileDto = BeanConvertUtil.covertBean(reqJson, FileDto.class);

        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));

        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

        JSONObject outParam = new JSONObject();
        outParam.put("fileId", fileName);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(outParam.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
