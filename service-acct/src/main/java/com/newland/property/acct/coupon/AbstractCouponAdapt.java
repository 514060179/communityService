package com.newland.property.acct.coupon;

import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;

public abstract class AbstractCouponAdapt implements ICouponAdapt{


    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {
        return new CouponQrCodeDto(couponPropertyUserDto.getCouponId(),"no data","优惠券不支持生成核销码");
    }
}
