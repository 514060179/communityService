package com.newland.property.acct.coupon.goods;

import com.newland.property.acct.coupon.AbstractCouponAdapt;
import com.newland.property.acct.coupon.ICouponAdapt;
import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;
import com.newland.property.intf.scm.ISupplierV1InnerServiceSMO;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(ICouponAdapt.COUPON_PRE + "1001")
public class ThridGoodsCouponAdapt extends AbstractCouponAdapt {

    /**
     * 供应商
     */
    @Autowired
    private ISupplierV1InnerServiceSMO supplierV1InnerServiceSMOImpl;

    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {
        //非法调入
        if (StringUtil.isEmpty(couponPropertyUserDto.getCouponId())) {
            return super.generatorQrcode(couponPropertyUserDto);
        }

        return supplierV1InnerServiceSMOImpl.generatorQrcode(couponPropertyUserDto);

    }
}
