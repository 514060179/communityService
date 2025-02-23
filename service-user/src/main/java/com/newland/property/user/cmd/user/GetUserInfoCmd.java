package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.NewlandPropertyCmdDoc;
import com.newland.property.doc.annotation.NewlandPropertyExampleDoc;
import com.newland.property.doc.annotation.NewlandPropertyParamDoc;
import com.newland.property.doc.annotation.NewlandPropertyResponseDoc;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.service.context.DataQuery;
import com.newland.property.service.smo.IQueryServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@NewlandPropertyCmdDoc(title = "查询用户信息",
        description = "查询用户信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/user.getUserInfo",
        resource = "userDoc",
        author = "simon feng",
        serviceCode = "user.getUserInfo",
        seq = 10
)


@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "storeTypeCd", type = "int", length = 11, defaultValue = "", remark = "编号"),
                @NewlandPropertyParamDoc(name = "address", type = "String", length = 250, defaultValue = "", remark = "地址"),
                @NewlandPropertyParamDoc(name = "watermark", type = "String", length = 250, defaultValue = "", remark = "二维码"),
                @NewlandPropertyParamDoc(name = "sex", type = "String", length = 1, defaultValue = "", remark = "性别"),
                @NewlandPropertyParamDoc(name = "name", type = "String", length = 250, defaultValue = "", remark = "姓名"),
                @NewlandPropertyParamDoc(name = "tel", type = "String", length = 250, defaultValue = "", remark = "电话"),
                @NewlandPropertyParamDoc(name = "localtionCd", type = "String", length = 2, defaultValue = "", remark = "登录状态"),
                @NewlandPropertyParamDoc(name = "levelCd", type = "String", length = 2, defaultValue = "", remark = "用户级别"),
                @NewlandPropertyParamDoc(name = "userId", type = "String", length = 64, defaultValue = "", remark = "用户ID"),
                @NewlandPropertyParamDoc(name = "email", type = "String", length = 250, defaultValue = "", remark = "邮件"),
                @NewlandPropertyParamDoc(name = "url", type = "String", length = 250, defaultValue = "", remark = "头像")
        }
)

@NewlandPropertyExampleDoc(
        resBody = "{\"storeTypeCd\":\"800900000003\",\"address\":\"珠海市\",\"watermark\":\"false\",\"sex\":\"1\",\"name\":\"YG002\",\"tel\":\"15919161025\",\"localtionCd\":\"\",\"levelCd\":\"01\",\"userId\":\"302024060613300410\",\"email\":\"514060179@qq.com\",\"url\":\"http://1231231\"}"
)

@NewlandPropertyCmd(serviceCode = "user.getUserInfo")
public class GetUserInfoCmd extends Cmd {

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");
        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.user.userInfo");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONObject tmpUserInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONObject resultUserInfo = new JSONObject();


        if (!StringUtil.isEmpty(storeId)) {
            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeId);
            storeDto.setPage(1);
            storeDto.setRow(1);
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
            if (storeDtos != null && storeDtos.size() > 0) {
                resultUserInfo.put("storeTypeCd", storeDtos.get(0).getStoreTypeCd());
            }
        }

        //获取头像
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(userId);
        resultUserInfo.put("url","");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() >= 1) {
            String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    resultUserInfo.put("url",tmpFileRelDto.getFileSaveName());
                } else {
                    resultUserInfo.put("url",imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
        resultUserInfo.put("name", tmpUserInfo.getString("name"));
        resultUserInfo.put("address", tmpUserInfo.getString("address"));
        resultUserInfo.put("sex", tmpUserInfo.getString("sex"));
        resultUserInfo.put("localtionCd", tmpUserInfo.getString("localtionCd"));
        resultUserInfo.put("levelCd", tmpUserInfo.getString("levelCd"));
        //resultUserInfo.put("tel", CommonUtil.mobileEncrypt(tmpUserInfo.getString("tel")));
        resultUserInfo.put("tel", tmpUserInfo.getString("tel")); // 这里不加密了 因为前台很多地方直接 关联出 用户的手机号 所以 加密了 没法处理 modify by wuxw 2022-07-04
        resultUserInfo.put("email", tmpUserInfo.getString("email"));
        resultUserInfo.put("userId", tmpUserInfo.getString("userId"));
        String watermark = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,"watermark");
        resultUserInfo.put("watermark", watermark);

        responseEntity = new ResponseEntity<String>(resultUserInfo.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
