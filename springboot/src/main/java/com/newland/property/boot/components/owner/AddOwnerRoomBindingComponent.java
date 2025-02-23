package com.newland.property.boot.components.owner;

import com.newland.property.boot.smo.addOwner.IAddOwnerRoomBindingSMO;
import com.newland.property.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加添加业主组件
 */
@Component("addOwnerRoomBinding")
public class AddOwnerRoomBindingComponent {

    @Autowired
    private IAddOwnerRoomBindingSMO addOwnerRoomBindingSMOImpl;

    /**
     * 添加添加业主数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd) {
        return addOwnerRoomBindingSMOImpl.bindingAddOwnerRoom(pd);
    }

    public IAddOwnerRoomBindingSMO getAddOwnerRoomBindingSMOImpl() {
        return addOwnerRoomBindingSMOImpl;
    }

    public void setAddOwnerRoomBindingSMOImpl(IAddOwnerRoomBindingSMO addOwnerRoomBindingSMOImpl) {
        this.addOwnerRoomBindingSMOImpl = addOwnerRoomBindingSMOImpl;
    }
}
