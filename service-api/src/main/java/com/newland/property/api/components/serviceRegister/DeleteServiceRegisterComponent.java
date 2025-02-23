package com.newland.property.api.components.serviceRegister;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.service.IDeleteServiceRegisterSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务绑定组件
 */
@Component("deleteServiceRegister")
public class DeleteServiceRegisterComponent {

@Autowired
private IDeleteServiceRegisterSMO deleteServiceRegisterSMOImpl;

/**
 * 添加服务绑定数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteServiceRegisterSMOImpl.deleteServiceRegister(pd);
    }

public IDeleteServiceRegisterSMO getDeleteServiceRegisterSMOImpl() {
        return deleteServiceRegisterSMOImpl;
    }

public void setDeleteServiceRegisterSMOImpl(IDeleteServiceRegisterSMO deleteServiceRegisterSMOImpl) {
        this.deleteServiceRegisterSMOImpl = deleteServiceRegisterSMOImpl;
    }
            }
