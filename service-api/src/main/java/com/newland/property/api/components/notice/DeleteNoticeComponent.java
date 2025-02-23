package com.newland.property.api.components.notice;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.notice.IDeleteNoticeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加公告组件
 */
@Component("deleteNotice")
public class DeleteNoticeComponent {

@Autowired
private IDeleteNoticeSMO deleteNoticeSMOImpl;

/**
 * 添加公告数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteNoticeSMOImpl.deleteNotice(pd);
    }

public IDeleteNoticeSMO getDeleteNoticeSMOImpl() {
        return deleteNoticeSMOImpl;
    }

public void setDeleteNoticeSMOImpl(IDeleteNoticeSMO deleteNoticeSMOImpl) {
        this.deleteNoticeSMOImpl = deleteNoticeSMOImpl;
    }
            }
