package com.newland.property.acct.coupon.goods;

import com.newland.property.acct.coupon.AbstractCouponAdapt;
import com.newland.property.acct.coupon.ICouponAdapt;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.coupon.CouponPropertyPoolDto;
import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;
import com.newland.property.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ICouponAdapt.COUPON_PRE + "1011")
public class GoodsCouponAdapt extends AbstractCouponAdapt {

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOmpl;

    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {

        //非法调入
        if (StringUtil.isEmpty(couponPropertyUserDto.getCouponId())) {
            return super.generatorQrcode(couponPropertyUserDto);
        }

        CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
        couponPropertyPoolDto.setCppId(couponPropertyUserDto.getCppId());
        couponPropertyPoolDto.setCommunityId(couponPropertyUserDto.getCommunityId());
        List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOmpl.queryCouponPropertyPools(couponPropertyPoolDto);

        Assert.listOnlyOne(couponPropertyPoolDtos, "优惠券不存在");

        String code = GenerateCodeFactory.getUUID();

        CommonCache.setValue(code, couponPropertyUserDto.getCouponId(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);

        return new CouponQrCodeDto(couponPropertyUserDto.getCouponId(), code, couponPropertyPoolDtos.get(0).getRemark());
    }
}
