package com.newland.property.api.components.service;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.service.IEditServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editService")
public class EditServiceComponent {

    @Autowired
    private IEditServiceSMO editServiceSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editServiceSMOImpl.updateService(pd);
    }

    public IEditServiceSMO getEditServiceSMOImpl() {
        return editServiceSMOImpl;
    }

    public void setEditServiceSMOImpl(IEditServiceSMO editServiceSMOImpl) {
        this.editServiceSMOImpl = editServiceSMOImpl;
    }
}
