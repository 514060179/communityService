package com.newland.property.api.components.notice;


import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.notice.IListNoticesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 公告组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("noticeDetail")
public class NoticeDetailComponent {

    @Autowired
    private IListNoticesSMO listNoticesSMOImpl;

    /**
     * 查询公告列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> get(IPageData pd){
        return listNoticesSMOImpl.listNotices(pd);
    }

    public IListNoticesSMO getListNoticesSMOImpl() {
        return listNoticesSMOImpl;
    }

    public void setListNoticesSMOImpl(IListNoticesSMO listNoticesSMOImpl) {
        this.listNoticesSMOImpl = listNoticesSMOImpl;
    }
}
