package com.newland.property.user.bmo.activitiesRule;
import com.newland.property.dto.activities.ActivitiesRuleDto;
import org.springframework.http.ResponseEntity;
public interface IGetActivitiesRuleBMO {


    /**
     * 查询活动规则
     * add by wuxw
     * @param  activitiesRuleDto
     * @return
     */
    ResponseEntity<String> get(ActivitiesRuleDto activitiesRuleDto);


}
