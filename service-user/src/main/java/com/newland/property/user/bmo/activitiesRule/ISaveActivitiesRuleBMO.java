package com.newland.property.user.bmo.activitiesRule;

import com.newland.property.po.activities.ActivitiesRulePo;
import org.springframework.http.ResponseEntity;
public interface ISaveActivitiesRuleBMO {


    /**
     * 添加活动规则
     * add by wuxw
     * @param activitiesRulePo
     * @return
     */
    ResponseEntity<String> save(ActivitiesRulePo activitiesRulePo);


}
