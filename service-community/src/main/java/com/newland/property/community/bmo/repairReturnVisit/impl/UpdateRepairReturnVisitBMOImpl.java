package com.newland.property.community.bmo.repairReturnVisit.impl;

import com.newland.property.community.bmo.repairReturnVisit.IUpdateRepairReturnVisitBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.newland.property.po.repair.RepairReturnVisitPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRepairReturnVisitBMOImpl")
public class UpdateRepairReturnVisitBMOImpl implements IUpdateRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    /**
     * @param repairReturnVisitPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RepairReturnVisitPo repairReturnVisitPo) {

        int flag = repairReturnVisitInnerServiceSMOImpl.updateRepairReturnVisit(repairReturnVisitPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
