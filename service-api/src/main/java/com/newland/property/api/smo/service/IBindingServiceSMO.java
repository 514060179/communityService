package com.newland.property.api.smo.service;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 服务绑定接口类
 */
public interface IBindingServiceSMO {

    //绑定服务
    public ResponseEntity<String> binding(IPageData pd);
}
