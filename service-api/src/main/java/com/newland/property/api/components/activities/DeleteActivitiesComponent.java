package com.newland.property.api.components.activities;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.common.ICommonPostSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加活动组件
 */
@Component("deleteActivities")
public class DeleteActivitiesComponent {

    @Autowired
    private ICommonPostSMO commonPostSMOImpl;

    /**
     * 添加活动数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        pd.setApiUrl("activities.deleteActivities");
        return commonPostSMOImpl.doService(pd);
    }

}
