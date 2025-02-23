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
import com.newland.property.dto.msg.SmsDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.common.ISmsInnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.user.*;
import com.newland.property.po.owner.OwnerAttrPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.po.owner.OwnerRoomRelPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 保存 业主
 */
@NewlandPropertyCmdDoc(title = "添加业主",
        description = "第三方系统，比如招商系统同步业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.saveOwner",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.saveOwner",
        seq = 9
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "name", length = 64, remark = "业主名称"),
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
@NewlandPropertyCmd(serviceCode = "owner.saveOwner")
public class SaveOwnerCmd extends Cmd {

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
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IGeneratorOwnerUserBMO generatorOwnerUserBMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含类型");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        //Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
        if (reqJson.containsKey("roomId")) {
            Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
            Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");
            Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
            Assert.hasLength(reqJson.getString("state"), "state不能为空");
            Assert.hasLength(reqJson.getString("storeId"), "storeId不能为空");
        }
        if (reqJson.containsKey("msgCode")) {
            SmsDto smsDto = new SmsDto();
            smsDto.setTel(reqJson.getString("link"));
            smsDto.setCode(reqJson.getString("msgCode"));
            smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);
            if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN,SendSmsFactory.SMS_SEND_SWITCH))) {
                throw new IllegalArgumentException(smsDto.getMsg());
            }
        }
        //属性校验
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if ((!reqJson.containsKey("source") || !"BatchImport".equals(reqJson.getString("source"))) && "ON".equals(userValidate)) {
            //获取手机号(判断手机号是否重复)
            String link = reqJson.getString("link");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(link);
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
            Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");
            //获取身份证号(判断身份证号是否重复)
            String idCard = reqJson.getString("idCard");
            if (!StringUtil.isEmpty(idCard)) {
                OwnerDto owner = new OwnerDto();
                owner.setIdCard(idCard);
                owner.setCommunityId(reqJson.getString("communityId"));
                List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryAllOwners(owner);
                Assert.listIsNull(owners, "身份证号重复，请重新输入");
            }
        }
        //生成memberId
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
        //有房屋信息，则直接绑定房屋和 业主的关系
        if (reqJson.containsKey("roomId")) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.putAll(reqJson);
            businessUnit.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
            flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }

        //保存照片
        photoSMOImpl.savePhoto(reqJson.getString("ownerPhoto"),
                reqJson.getString("memberId"),
                reqJson.getString("communityId"),
                "10000");

        dealOwnerAttr(reqJson, cmdDataFlowContext);

        // todo 生成登录账号
        generatorOwnerUserBMOImpl.generator(ownerPo);

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
        if (!paramObj.containsKey("ownerId") || OwnerDto.OWNER_TYPE_CD_OWNER.equals(paramObj.getString("ownerTypeCd"))) {
            paramObj.put("ownerId", memberId);
        }
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
