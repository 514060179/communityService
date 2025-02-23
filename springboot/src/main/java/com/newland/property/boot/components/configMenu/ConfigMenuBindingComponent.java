package com.newland.property.boot.components.configMenu;

import com.newland.property.boot.smo.configMenu.IConfigMenuBindingSMO;
import com.newland.property.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加配置菜单组件
 */
@Component("configMenuBinding")
public class ConfigMenuBindingComponent {

    @Autowired
    private IConfigMenuBindingSMO configMenuBindingSMOImpl;

    /**
     * 添加配置菜单数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return configMenuBindingSMOImpl.bindingConfigMenu(pd);
    }

    public IConfigMenuBindingSMO getConfigMenuBindingSMOImpl() {
        return configMenuBindingSMOImpl;
    }

    public void setConfigMenuBindingSMOImpl(IConfigMenuBindingSMO configMenuBindingSMOImpl) {
        this.configMenuBindingSMOImpl = configMenuBindingSMOImpl;
    }
}
