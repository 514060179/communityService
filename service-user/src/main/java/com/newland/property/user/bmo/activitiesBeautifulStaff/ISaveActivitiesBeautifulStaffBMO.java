package com.newland.property.user.bmo.activitiesBeautifulStaff;

import com.newland.property.po.activities.ActivitiesBeautifulStaffPo;
import org.springframework.http.ResponseEntity;
public interface ISaveActivitiesBeautifulStaffBMO {


    /**
     * 添加活动规则
     * add by wuxw
     * @param activitiesBeautifulStaffPo
     * @return
     */
    ResponseEntity<String> save(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo);


}
