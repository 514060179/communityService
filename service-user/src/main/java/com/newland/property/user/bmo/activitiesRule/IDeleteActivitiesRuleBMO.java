package com.newland.property.user.bmo.activitiesRule;
import com.newland.property.po.activities.ActivitiesRulePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteActivitiesRuleBMO {


    /**
     * 修改活动规则
     * add by wuxw
     * @param activitiesRulePo
     * @return
     */
    ResponseEntity<String> delete(ActivitiesRulePo activitiesRulePo);


}
