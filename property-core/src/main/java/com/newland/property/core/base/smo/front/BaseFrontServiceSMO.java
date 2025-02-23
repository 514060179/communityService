package com.newland.property.core.base.smo.front;

import com.newland.property.core.base.smo.BaseServiceSMO;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName BaseFrontServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/15 21:22
 * @Version 1.0
 * add by wuxw 2020/6/15
 **/
public class BaseFrontServiceSMO extends BaseServiceSMO {

    private static final String URL_API =  "";
    //日志
    private static Logger logger = LoggerFactory.getLogger(BaseFrontServiceSMO.class);

    @Autowired
    private RestTemplate restTemplate;





}
