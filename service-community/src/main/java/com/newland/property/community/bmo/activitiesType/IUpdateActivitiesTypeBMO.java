package com.newland.property.community.bmo.activitiesType;
import com.newland.property.po.activities.ActivitiesTypePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateActivitiesTypeBMO {


    /**
     * 修改信息分类
     * add by wuxw
     * @param activitiesTypePo
     * @return
     */
    ResponseEntity<String> update(ActivitiesTypePo activitiesTypePo);


}
