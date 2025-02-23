package com.newland.property.community.smo.impl;

import com.newland.property.community.dao.IResourceStoreServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.community.IResourceStoreServiceSMO;
import com.newland.property.po.purchase.ResourceStorePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResourceStoreServiceSMOImpl extends BaseServiceSMO implements IResourceStoreServiceSMO {

    @Autowired
    private IResourceStoreServiceDao resourceStoreServiceDao;

    @Override
    public List<ResourceStorePo> getResourceStores(@RequestBody ResourceStorePo resourceStorePo) {
        List<ResourceStorePo> resourceStorePos = BeanConvertUtil.covertBeanList(resourceStoreServiceDao.getResourceStoresInfo(BeanConvertUtil.beanCovertMap(resourceStorePo)), ResourceStorePo.class);
        return resourceStorePos;
    }

    @Override
    public int getResourceStoresCount(@RequestBody ResourceStorePo resourceStorePo) {
        return resourceStoreServiceDao.getResourceStoresCount(BeanConvertUtil.beanCovertMap(resourceStorePo));
    }
}
