package com.newland.property.api.smo.payment;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 统一下单接口类
 */
public interface IToQrPayOweFeeSMO {

    /**
     * 下单
     * @param pd
     * @return
     */
    public ResponseEntity<String> toPay(IPageData pd);
}
