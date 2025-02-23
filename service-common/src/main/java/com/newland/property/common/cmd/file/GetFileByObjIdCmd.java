package com.newland.property.common.cmd.file;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.file.FileDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.file.ApiFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "file.getFileByObjId")
public class GetFileByObjIdCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson,"objId", "未包含文件ID");
        Assert.hasKeyAndValue(reqJson,"fileTypeCd", "未包含文件类型");
        Assert.hasKeyAndValue(reqJson,"communityId", "未包含小区ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("objId"));
        fileRelDto.setRelTypeCd(reqJson.getString("fileTypeCd"));

        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        Assert.listOnlyOne(fileRelDtos,"存在多条数据或未存在数据");

        FileDto fileDto = BeanConvertUtil.covertBean(reqJson, FileDto.class);
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);

        Assert.listOnlyOne(fileDtos,"存在多个文件，数据错误" + reqJson.toJSONString());

        ApiFileVo fileVo = BeanConvertUtil.covertBean(fileDtos.get(0), ApiFileVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(fileVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
