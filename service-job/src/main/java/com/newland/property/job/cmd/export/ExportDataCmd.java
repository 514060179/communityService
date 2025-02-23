package com.newland.property.job.cmd.export;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.user.UserDownloadFileDto;
import com.newland.property.intf.job.IUserDownloadFileV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.job.export.ExportDataQueue;
import com.newland.property.po.user.UserDownloadFilePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 数据导出处理类
 */
@NewlandPropertyCmd(serviceCode = "export.exportData")
public class ExportDataCmd extends Cmd {

    private static final String EXPORT_DATA_PRE = "temp/export/data/";

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IUserDownloadFileV1InnerServiceSMO userDownloadFileV1InnerServiceSMOImpl;


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(reqJson, "pagePath", "请求中未包含页面");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        //这里放入 员工ID 和  商户ID

        reqJson.put("userId",userId);
        reqJson.put("storeId",storeId);

        ExportDataDto exportDataDto = new ExportDataDto();
        exportDataDto.setBusinessAdapt(reqJson.getString("pagePath"));
        exportDataDto.setReqJson(reqJson);
        String fileName = GenerateCodeFactory.getUUID()
                + ".xlsx";
        exportDataDto.setFileName(EXPORT_DATA_PRE
                + reqJson.getString("pagePath")
                + "/"
                + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)
                + "/"
                + fileName);

        UserDownloadFilePo userDownloadFilePo = new UserDownloadFilePo();
        userDownloadFilePo.setDownloadId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        userDownloadFilePo.setDownloadUserId(userId);
        userDownloadFilePo.setDownloadUserName(userDtos.get(0).getName());
        userDownloadFilePo.setFileType(reqJson.getString("pagePath"));
        userDownloadFilePo.setCommunityId(reqJson.getString("communityId"));
        userDownloadFilePo.setName(fileName);
        userDownloadFilePo.setState(UserDownloadFileDto.STATE_WAIT);
        int flag = userDownloadFileV1InnerServiceSMOImpl.saveUserDownloadFile(userDownloadFilePo);

        if (flag < 1) {
            throw new CmdException("下载文件失败");
        }

        exportDataDto.setDownloadId(userDownloadFilePo.getDownloadId());

        ExportDataQueue.addMsg(exportDataDto);


        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK,"文件正在生成，请到文件下载页面下载"));
    }
}
