package com.newland.property.community.bmo.activitiesType;

import com.newland.property.po.activities.ActivitiesTypePo;
import org.springframework.http.ResponseEntity;
public interface ISaveActivitiesTypeBMO {


    /**
     * 添加信息分类
     * add by wuxw
     * @param activitiesTypePo
     * @return
     */
    ResponseEntity<String> save(ActivitiesTypePo activitiesTypePo);


}
