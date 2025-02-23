package com.newland.property.acct.coupon;

import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;

public interface ICouponAdapt {

    public static final String COUPON_PRE= "couponProperty";

    /**
     * 生成 优惠券核销二维码
     * @param couponPropertyUserDto
     * @return
     */
    CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto);
}
