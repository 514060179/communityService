package com.newland.property.common.cmd.app;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.app.AppVersionDto;
import com.newland.property.intf.common.IAppVersionInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import com.newland.property.vo.api.app.ApiAppVersionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NewlandPropertyCmdDoc(title = "获取App版本信息",
        description = "业主端和物业端App可通过此接口获取到App版本信息<br/>" +
                "然后根据获取到的信息判断是否要弹出版本更新弹窗<br/>" +
                "注意：强更的情况下，不能取消，必须更新后才能进入App</br>",
        httpMethod = "GET",
        url = "http://{ip}:{port}/app/app.getAppVersion",
        resource = "commonDoc",
        author = "Moonny",
        serviceCode = "app.getAppVersion",
        seq = 23
)

@NewlandPropertyParamsDoc(
        headers = {
                @NewlandPropertyHeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
                @NewlandPropertyHeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
                @NewlandPropertyHeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
                @NewlandPropertyHeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
        },
        params = {
                @NewlandPropertyParamDoc(name = "platform", type = "int", remark = "0 表示 iOS, 1 表示安卓"),
                @NewlandPropertyParamDoc(name = "channel", remark = "渠道，iOS: appStore, " +
                        "安卓：googleStore, 后续安卓上了别的应用市场会有不同值"),
                @NewlandPropertyParamDoc(name = "lang", remark = "语言，zh-Hant 繁体  zh-Hans 简体"),
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "appId", remark = "应用appId"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "channel", remark = "渠道"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "code", remark = "最新版本号"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "desc", remark =
                        "版本更新内容，内容较长时需要换行"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "id", type = "int", remark = "id"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "isForceUpdate", type = "int", remark =
                        "是否强制更新 0 否 1 是"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "isOpenTip", type = "int", remark = "是否弹窗提示，0" +
                        " 否 1是"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "name"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "platform"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "url", remark = "下载App地址"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/app.getAppVersion?platform=0&channel=appStore&lang=zh-Hant",
        resBody="{\n" +
                "\t\"code\": 0,\n" +
                "\t\"data\": {\n" +
                "\t\t\"appId\": \"992020061452450002\",\n" +
                "\t\t\"channel\": \"appStore\",\n" +
                "\t\t\"code\": \"0.0.2\",\n" +
                "\t\t\"desc\": \"1. 控制App賬號登錄;\\n2. 新增App版本升級提示功能;\\n3. 修復已知問題。\",\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"inAppUpdate\": 0,\n" +
                "\t\t\"isForceUpdate\": 0,\n" +
                "\t\t\"isOpenTip\": 1,\n" +
                "\t\t\"name\": \"iOS\",\n" +
                "\t\t\"platform\": \"0\",\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"updateTime\": \"2024-06-13 09:31:37\",\n" +
                "\t\t\"url\": \"https://m.newlandgo.com\"\n" +
                "\t},\n" +
                "\t\"msg\": \"成功\"\n" +
                "}"
)

/*
  @author Moonny
 */
@NewlandPropertyCmd(serviceCode = "app.getAppVersion")
public class GetAppVersionCmd extends Cmd {

    @Autowired
    private IAppVersionInnerServiceSMO appVersionInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "platform", "未包含platform");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String appId = context.getReqHeaders().get("app-id");

        String platform = String.valueOf(reqJson.get("platform"));
        String channel = "";
        if(reqJson.containsKey("channel")) {
            channel = reqJson.getString("channel");
        }

        if ("0".equals(platform)) {
            if (StringUtil.isEmpty(channel)) {
                channel = "appStore";
            }
        } else {
            if (StringUtil.isEmpty(channel)) {
                channel = "googleStore";
            }
        }

        String lang = "";
        if(reqJson.containsKey("lang")) {
            lang = reqJson.getString("lang");
        }
        if (StringUtil.isEmpty(lang)) {
            lang = "zh-Hant";
        }

        List<String> langs = new ArrayList<>();
        langs.add("zh-Hant");
        langs.add("zh-Hans");
        if(!langs.contains(lang)) {
            lang = "zh-Hant";
        }

        AppVersionDto appVersionDto = new AppVersionDto();
        appVersionDto.setAppId(appId);
        appVersionDto.setPlatform(platform);
        appVersionDto.setChannel(channel);
        appVersionDto.setLang(lang);

        List<ApiAppVersionVo> versions =
                BeanConvertUtil.covertBeanList(appVersionInnerServiceSMO.getAppVersion(appVersionDto), ApiAppVersionVo.class);

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, versions.isEmpty() ? new JSONObject() : versions.get(0));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
