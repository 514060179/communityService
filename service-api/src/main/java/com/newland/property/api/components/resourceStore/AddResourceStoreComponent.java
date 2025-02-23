package com.newland.property.api.components.resourceStore;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.resourceStore.IAddResourceStoreSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加物品管理组件
 */
@Component("addResourceStore")
public class AddResourceStoreComponent {

    @Autowired
    private IAddResourceStoreSMO addResourceStoreSMOImpl;

    /**
     * 添加物品管理数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addResourceStoreSMOImpl.saveResourceStore(pd);
    }

    public IAddResourceStoreSMO getAddResourceStoreSMOImpl() {
        return addResourceStoreSMOImpl;
    }

    public void setAddResourceStoreSMOImpl(IAddResourceStoreSMO addResourceStoreSMOImpl) {
        this.addResourceStoreSMOImpl = addResourceStoreSMOImpl;
    }
}
