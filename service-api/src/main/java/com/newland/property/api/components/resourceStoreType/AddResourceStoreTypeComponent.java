package com.newland.property.api.components.resourceStoreType;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.resourceStoreType.IAddResourceStoreTypeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加物品管理组件
 */
@Component("addResourceStoreType")
public class AddResourceStoreTypeComponent {

    @Autowired
    private IAddResourceStoreTypeSMO addResourceStoreTypeSMOImpl;

    /**
     * 添加物品管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addResourceStoreTypeSMOImpl.saveResourceStoreType(pd);
    }

    public IAddResourceStoreTypeSMO getAddResourceStoreTypeSMOImpl() {
        return addResourceStoreTypeSMOImpl;
    }

    public void setAddResourceStoreTypeSMOImpl(IAddResourceStoreTypeSMO addResourceStoreTypeSMOImpl) {
        this.addResourceStoreTypeSMOImpl = addResourceStoreTypeSMOImpl;
    }
}
