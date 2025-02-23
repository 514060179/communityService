package com.newland.property.api.components.community;

import com.newland.property.api.smo.community.IDeleteCommunitySMO;
import com.newland.property.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加小区组件
 */
@Component("deleteCommunity")
public class DeleteCommunityComponent {

    @Autowired
    private IDeleteCommunitySMO deleteCommunitySMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteCommunitySMOImpl.deleteCommunity(pd);
    }

    public IDeleteCommunitySMO getDeleteCommunitySMOImpl() {
        return deleteCommunitySMOImpl;
    }

    public void setDeleteCommunitySMOImpl(IDeleteCommunitySMO deleteCommunitySMOImpl) {
        this.deleteCommunitySMOImpl = deleteCommunitySMOImpl;
    }
}
