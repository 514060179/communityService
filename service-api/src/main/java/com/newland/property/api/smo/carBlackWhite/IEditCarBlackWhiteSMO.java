package com.newland.property.api.smo.carBlackWhite;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改黑白名单接口
 *
 * add by wuxw 2019-06-30
 */
public interface IEditCarBlackWhiteSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateCarBlackWhite(IPageData pd);
}
