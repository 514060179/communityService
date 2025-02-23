package com.newland.property.api.smo.impl;

import com.newland.property.api.smo.ITestServiceSMO;
import com.newland.property.core.annotation.PropertyTransactional;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestServiceSMOImpl implements ITestServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(TestServiceSMOImpl.class);

    @Override
    @PropertyTransactional
    public String getVersion(String name) {
        logger.debug("调用方法调用");
        return "123";
    }
}
