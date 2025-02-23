package com.newland.property.user.bmo.activitiesBeautifulStaff.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.newland.property.po.activities.ActivitiesBeautifulStaffPo;
import com.newland.property.user.bmo.activitiesBeautifulStaff.IUpdateActivitiesBeautifulStaffBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateActivitiesBeautifulStaffBMOImpl")
public class UpdateActivitiesBeautifulStaffBMOImpl implements IUpdateActivitiesBeautifulStaffBMO {

    @Autowired
    private IActivitiesBeautifulStaffInnerServiceSMO activitiesBeautifulStaffInnerServiceSMOImpl;

    /**
     * @param activitiesBeautifulStaffPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {

        int flag = activitiesBeautifulStaffInnerServiceSMOImpl.updateActivitiesBeautifulStaff(activitiesBeautifulStaffPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
