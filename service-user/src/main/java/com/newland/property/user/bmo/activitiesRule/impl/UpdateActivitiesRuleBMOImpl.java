package com.newland.property.user.bmo.activitiesRule.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IActivitiesRuleInnerServiceSMO;
import com.newland.property.po.activities.ActivitiesRulePo;
import com.newland.property.user.bmo.activitiesRule.IUpdateActivitiesRuleBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateActivitiesRuleBMOImpl")
public class UpdateActivitiesRuleBMOImpl implements IUpdateActivitiesRuleBMO {

    @Autowired
    private IActivitiesRuleInnerServiceSMO activitiesRuleInnerServiceSMOImpl;

    /**
     * @param activitiesRulePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ActivitiesRulePo activitiesRulePo) {

        int flag = activitiesRuleInnerServiceSMOImpl.updateActivitiesRule(activitiesRulePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
