package com.newland.property.community.bmo.activitiesType;
import com.newland.property.dto.activities.ActivitiesTypeDto;
import org.springframework.http.ResponseEntity;
public interface IGetActivitiesTypeBMO {


    /**
     * 查询信息分类
     * add by wuxw
     * @param  activitiesTypeDto
     * @return
     */
    ResponseEntity<String> get(ActivitiesTypeDto activitiesTypeDto);


}
