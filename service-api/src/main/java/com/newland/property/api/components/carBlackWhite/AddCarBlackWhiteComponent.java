package com.newland.property.api.components.carBlackWhite;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.carBlackWhite.IAddCarBlackWhiteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加黑白名单组件
 */
@Component("addCarBlackWhite")
public class AddCarBlackWhiteComponent {

    @Autowired
    private IAddCarBlackWhiteSMO addCarBlackWhiteSMOImpl;

    /**
     * 添加黑白名单数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addCarBlackWhiteSMOImpl.saveCarBlackWhite(pd);
    }

    public IAddCarBlackWhiteSMO getAddCarBlackWhiteSMOImpl() {
        return addCarBlackWhiteSMOImpl;
    }

    public void setAddCarBlackWhiteSMOImpl(IAddCarBlackWhiteSMO addCarBlackWhiteSMOImpl) {
        this.addCarBlackWhiteSMOImpl = addCarBlackWhiteSMOImpl;
    }
}
