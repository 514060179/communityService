package com.newland.property.common.smo.impl;

import com.newland.property.common.dao.IAppVersionServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.app.AppVersionDto;
import com.newland.property.intf.common.IAppVersionInnerServiceSMO;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppVersionInnerServiceSMOImpl  extends BaseServiceSMO implements IAppVersionInnerServiceSMO {

    @Autowired
    private IAppVersionServiceDao appVersionServiceDaoImpl;

    @Override
    public List<AppVersionDto> getAppVersion(AppVersionDto appVersionDto) {
        List<AppVersionDto> versions =
                BeanConvertUtil.covertBeanList(appVersionServiceDaoImpl.getAppVersion(BeanConvertUtil.beanCovertMap(appVersionDto)),
                        AppVersionDto.class);

        return versions;
    }
}
