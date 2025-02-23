package com.newland.property.user.bmo.activitiesBeautifulStaff.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.newland.property.po.activities.ActivitiesBeautifulStaffPo;
import com.newland.property.user.bmo.activitiesBeautifulStaff.ISaveActivitiesBeautifulStaffBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveActivitiesBeautifulStaffBMOImpl")
public class SaveActivitiesBeautifulStaffBMOImpl implements ISaveActivitiesBeautifulStaffBMO {

    @Autowired
    private IActivitiesBeautifulStaffInnerServiceSMO activitiesBeautifulStaffInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param activitiesBeautifulStaffPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {

        activitiesBeautifulStaffPo.setBeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_beId));
        activitiesBeautifulStaffPo.setPoll("0");
        int flag = activitiesBeautifulStaffInnerServiceSMOImpl.saveActivitiesBeautifulStaff(activitiesBeautifulStaffPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
