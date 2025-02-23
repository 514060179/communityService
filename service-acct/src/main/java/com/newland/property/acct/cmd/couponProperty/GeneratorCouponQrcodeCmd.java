package com.newland.property.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.acct.coupon.ICouponAdapt;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;



@NewlandPropertyCmdDoc(title = "优惠券核销码",
        description = "供业主端生成优惠券核销码",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/couponProperty.generatorCouponQrcode",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "couponProperty.generatorCouponQrcode"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "qrCode", type = "String", remark = "二维码信息"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "remark", type = "String", remark = "优惠券核销说明"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{'couponId':'123123'}",
        resBody = "{'code':0,'msg':'成功','data':{'qrCode':'123123','remark':'nihao'}}"
)

@NewlandPropertyCmd(serviceCode = "couponProperty.generatorCouponQrcode")
public class GeneratorCouponQrcodeCmd extends Cmd {

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "couponId", "未包含优惠券ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");

        //校验优惠券是否存在
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos,"用户不存在");

        CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
        couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
        couponPropertyUserDto.setTel(userDtos.get(0).getTel());
        couponPropertyUserDto.setState(CouponPropertyUserDto.STATE_WAIT);

        List<CouponPropertyUserDto> couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

        if (couponPropertyUserDtos == null || couponPropertyUserDtos.size() < 1) {
            throw new CmdException("优惠券不存在");
        }

        if (!"Y".equals(couponPropertyUserDtos.get(0).getIsExpire())) {
            throw new CmdException("优惠券已过期");
        }


        String toType = couponPropertyUserDtos.get(0).getToType();

        ICouponAdapt couponAdapt = ApplicationContextFactory.getBean(ICouponAdapt.COUPON_PRE+toType,ICouponAdapt.class);

        if(couponAdapt == null){
            throw new CmdException("优惠券不支持生成二维码");
        }

        CouponQrCodeDto couponQrCodeDto = couponAdapt.generatorQrcode(couponPropertyUserDtos.get(0));

        context.setResponseEntity(ResultVo.createResponseEntity(couponQrCodeDto));
    }
}
