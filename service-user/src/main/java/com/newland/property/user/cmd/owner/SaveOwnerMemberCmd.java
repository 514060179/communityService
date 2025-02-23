package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.factory.SendSmsFactory;
import com.newland.property.core.smo.IPhotoSMO;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.msg.SmsDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IAuditUserInnerServiceSMO;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.common.ISmsInnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.jpush.ISendJPushMessageAdapt;
import com.newland.property.intf.user.*;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.po.owner.OwnerAttrPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmdDoc(title = "添加业主",
        description = "第三方系统，比如招商系统同步业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.saveOwnerMember",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.saveOwnerMember",
        seq = 10
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "name", length = 64, remark = "业主名称"),
        @NewlandPropertyParamDoc(name = "roomName", length = 64, remark = "房屋 楼栋-单元-房屋"),
        @NewlandPropertyParamDoc(name = "link", length = 11, remark = "业主手机号"),
        @NewlandPropertyParamDoc(name = "idCard", length = 30, remark = "业主身份证号"),
        @NewlandPropertyParamDoc(name = "address", length = 512, remark = "地址"),
        @NewlandPropertyParamDoc(name = "sex", length = 12, remark = "性别 男 1 女 0"),
        @NewlandPropertyParamDoc(name = "ownerTypeCd", length = 12, remark = "业主类型 1001 业主 2002 家庭成员 家庭成员 需要传业主的ownerId"),
        @NewlandPropertyParamDoc(name = "remark", length = 512, remark = "备注"),
        @NewlandPropertyParamDoc(name = "ownerId", length = 30, remark = "业主 时 填写-1 家庭成员时填写业主ID"),
        @NewlandPropertyParamDoc(name = "ownerPhoto", length = -1, remark = "业主人脸 用于同步门禁 人脸开门"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\n" +
                "\t\"name\": \"王王\",\n" +
                "\t\"roomName\": \"1-1-1001\",\n" +
                "\t\"age\": \"\",\n" +
                "\t\"link\": \"18909718888\",\n" +
                "\t\"address\": \"张三\",\n" +
                "\t\"sex\": \"0\",\n" +
                "\t\"ownerTypeCd\": \"1001\",\n" +
                "\t\"remark\": \"\",\n" +
                "\t\"ownerId\": -1,\n" +
                "\t\"ownerPhoto\": \"\",\n" +
                "\t\"idCard\": \"\",\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
/**
 * 添加家庭成员
 */
@NewlandPropertyCmd(serviceCode = "owner.saveOwnerMember")
public class SaveOwnerMemberCmd extends Cmd {


    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IGeneratorOwnerUserBMO generatorOwnerUserBMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含业主");

        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        //todo 验证吗校验
        if (reqJson.containsKey("msgCode")) {
            SmsDto smsDto = new SmsDto();
            smsDto.setTel(reqJson.getString("link"));
            smsDto.setCode(reqJson.getString("msgCode"));
            smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);
            if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN, SendSmsFactory.SMS_SEND_SWITCH))) {
                throw new IllegalArgumentException(smsDto.getMsg());
            }
        }
        //todo 校验手机号重复
        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if ("ON".equals(userValidate)) {
            String link = reqJson.getString("link");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(link);
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
            Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");
        }

        //todo 属性校验
        Assert.judgeAttrValue(reqJson);

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        //todo 生成memberId
        generateMemberId(reqJson);

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(reqJson);
        businessOwner.put("state", "2000");
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        if (reqJson.containsKey("age") && StringUtil.isEmpty(reqJson.getString("age"))) {
            ownerPo.setAge(null);
        }
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }

        //todo 保存照片
        photoSMOImpl.savePhoto(reqJson.getString("ownerPhoto"),
                reqJson.getString("memberId"),
                reqJson.getString("communityId"),
                "10000");

        //todo 保存属性
        dealOwnerAttr(reqJson, context);

        //todo 生成登录账号
        generatorOwnerUserBMOImpl.generator(ownerPo);

        // todo 添加成员成功发推送-jpush
        // appPage: /pages/family/familyList

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        UserDto userDto = new UserDto();
        userDto.setTel(ownerDtos.get(0).getLink());
        userDto.setStatusCd("0");
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        ISendJPushMessageAdapt adapt = ApplicationContextFactory.getBean("addMemberSendMessageAdapt", ISendJPushMessageAdapt.class);
        if (adapt != null) {
            adapt.sendMessage(ownerPo.getCommunityId(), userDtos.get(0).getUserId());
        }
//        JPushMessageDto messageDto = new JPushMessageDto();
//        messageDto.setCommunityId(ownerPo.getCommunityId());
//        messageDto.setUserId(userDtos.get(0).getUserId());
//        messageDto.setPageUrl("/pages/family/familyList");
//        messageDto.setTitle("家庭成员");
//        messageDto.setContent("添加家庭成员成功");
//        messageDto.setPageType("appPage");
//        JPushMessageQueue.addMsg(messageDto);

        // todo 同步业主信息到iot
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_TO_IOT, reqJson));
    }


    /**
     * 生成小区楼ID
     *
     * @param paramObj 请求入参数据
     */
    private void generateMemberId(JSONObject paramObj) {
        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramObj.put("memberId", memberId);

    }

    private void dealOwnerAttr(JSONObject paramObj, ICmdDataFlowContext cmdDataFlowContext) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }

        int flag = 0;
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("memberId", paramObj.getString("memberId"));
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
            flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }

    }
}
