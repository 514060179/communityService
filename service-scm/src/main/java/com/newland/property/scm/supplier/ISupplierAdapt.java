package com.newland.property.scm.supplier;

import com.newland.property.dto.coupon.CouponPropertyUserDto;
import com.newland.property.dto.coupon.CouponQrCodeDto;
import com.newland.property.dto.supplier.SupplierDto;
import com.newland.property.dto.supplier.SupplierCouponDto;

public interface ISupplierAdapt {

    /**
     * 生成核销 码
     * @param couponPropertyUserDto
     * @param supplierDto
     * @param supplierCouponDto
     * @return
     */
    CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto, SupplierDto supplierDto, SupplierCouponDto supplierCouponDto);
}
