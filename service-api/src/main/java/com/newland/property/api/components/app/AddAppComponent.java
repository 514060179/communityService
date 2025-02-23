package com.newland.property.api.components.app;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.app.IAddAppSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加应用组件
 */
@Component("addApp")
public class AddAppComponent {

    @Autowired
    private IAddAppSMO addAppSMOImpl;

    /**
     * 添加应用数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addAppSMOImpl.saveApp(pd);
    }

    public IAddAppSMO getAddAppSMOImpl() {
        return addAppSMOImpl;
    }

    public void setAddAppSMOImpl(IAddAppSMO addAppSMOImpl) {
        this.addAppSMOImpl = addAppSMOImpl;
    }
}
