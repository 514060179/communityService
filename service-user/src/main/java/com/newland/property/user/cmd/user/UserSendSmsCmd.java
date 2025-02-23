package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.SendSmsFactory;
import com.newland.property.dto.msg.SmsDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.common.ISmsInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.lock.DistributedLock;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.utils.util.ValidatorUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "user.userSendSms")
public class UserSendSmsCmd extends Cmd {

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    public static final String ID_CARD_SWITCH = "ID_CARD_SWITCH";

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");

        String areaCode = "";
        if(reqJson.containsKey("areaCode")){
            areaCode = reqJson.getString("areaCode");
        }

        if(StringUtil.isEmpty(areaCode)) {
            areaCode = "+86";
        }

        if("+86".equals(areaCode)) {
            if (!ValidatorUtil.isMobile(reqJson.getString("tel"))) {
                throw new IllegalArgumentException("手机号格式错误");
            }
        }

        String captchaType = reqJson.getString("captchaType");
        if (!StringUtil.isEmpty(captchaType) && "ownerBinding".equals(captchaType)) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            ownerDto.setName(reqJson.getString("appUserName"));
            ownerDto.setLink(reqJson.getString("tel"));

            //取出开关映射的值
            String val = MappingCache.getValue(DOMAIN_COMMON, ID_CARD_SWITCH);
            //取出身份证
            String idCardErrorMsg = "";
            String idCard = reqJson.getString("idCard");
            if ("1".equals(val) && !StringUtil.isEmpty(idCard)) {
                ownerDto.setIdCard(idCard);
                idCardErrorMsg = "或者身份证号";
            }
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
            Assert.listOnlyOne(ownerDtos, "填写业主信息错误，请确认，预留业主姓名、手机号" + idCardErrorMsg + "信息是否正确！");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("tel");
        String areaCode = "";
        String phone = reqJson.getString("tel");

        if(reqJson.containsKey("areaCode")) {
            areaCode = reqJson.getString("areaCode");
        }

        if(StringUtil.isEmpty(areaCode)) {
            if (phone.length() == 8) {
                areaCode = "+853";
            } else {
                areaCode = "+86";
            }
        }

        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            //校验是否有有效的验证码
            String smsCode = CommonCache.getValue( areaCode + phone + SendSmsFactory.VALIDATE_CODE);

            if (!StringUtil.isEmpty(smsCode) && smsCode.contains("-")) {
                long oldTime = Long.parseLong(smsCode.substring(smsCode.indexOf("-") + 1, smsCode.length()));
                long nowTime = new Date().getTime();
                if (nowTime - oldTime < 1000 * 60 * 2) {
                    throw new IllegalArgumentException("请不要重复发送验证码");
                }
            }

            //校验是否传了 分页信息
            String msgCode = SendSmsFactory.generateMessageCode(6);
            SmsDto smsDto = new SmsDto();
            smsDto.setAreCode(areaCode);
            smsDto.setTel(phone);
            smsDto.setCode(msgCode);
            if ("ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN, SendSmsFactory.SMS_SEND_SWITCH))) {
                smsDto = smsInnerServiceSMOImpl.send(smsDto);
            } else {
                CommonCache.setValue(smsDto.getAreaCode() + smsDto.getTel() + SendSmsFactory.VALIDATE_CODE,
                        smsDto.getCode().toLowerCase() + "-" + new Date().getTime(), CommonCache.defaultExpireTime);
                smsDto.setSuccess(true);
                smsDto.setMsg("您的验证码为" + msgCode);
            }

            context.setResponseEntity(ResultVo.createResponseEntity(smsDto.getMsg()));
        } finally {
            //清理事务信息
            DistributedLock.releaseDistributedLock(key, requestId);
        }
    }
}
