package com.newland.property.boot.smo.sys;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface ISysServiceSMO {

    ResponseEntity<String> getSysInfo(IPageData pd);
}
