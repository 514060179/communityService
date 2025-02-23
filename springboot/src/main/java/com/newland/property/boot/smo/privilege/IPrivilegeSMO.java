package com.newland.property.boot.smo.privilege;

import com.newland.property.core.context.IPageData;
import org.springframework.web.client.RestTemplate;

public interface IPrivilegeSMO {

    void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource);
}
