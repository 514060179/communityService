package com.newland.property.community.bmo.repairReturnVisit.impl;

import com.newland.property.community.bmo.repairReturnVisit.IDeleteRepairReturnVisitBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.newland.property.po.repair.RepairReturnVisitPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRepairReturnVisitBMOImpl")
public class DeleteRepairReturnVisitBMOImpl implements IDeleteRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    /**
     * @param repairReturnVisitPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RepairReturnVisitPo repairReturnVisitPo) {

        int flag = repairReturnVisitInnerServiceSMOImpl.deleteRepairReturnVisit(repairReturnVisitPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
