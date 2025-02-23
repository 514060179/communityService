package com.newland.property.api.components.activities;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.common.ICommonGetSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 活动组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("activitiesManage")
public class ActivitiesManageComponent {

    @Autowired
    private ICommonGetSMO commonGetSMOImpl;

    /**
     * 查询活动列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        pd.setApiUrl("activities.listActivitiess");
        return commonGetSMOImpl.doService(pd);
    }
}
