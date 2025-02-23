package com.newland.property.community.bmo.activitiesType.impl;

import com.newland.property.community.bmo.activitiesType.IUpdateActivitiesTypeBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.community.IActivitiesTypeInnerServiceSMO;
import com.newland.property.po.activities.ActivitiesTypePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateActivitiesTypeBMOImpl")
public class UpdateActivitiesTypeBMOImpl implements IUpdateActivitiesTypeBMO {

    @Autowired
    private IActivitiesTypeInnerServiceSMO activitiesTypeInnerServiceSMOImpl;

    /**
     * @param activitiesTypePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ActivitiesTypePo activitiesTypePo) {

        int flag = activitiesTypeInnerServiceSMOImpl.updateActivitiesType(activitiesTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
