package com.newland.property.store.bmo.purchase.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.purchase.PurchaseApplyDto;
import com.newland.property.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.newland.property.intf.store.IPurchaseApplyInnerServiceSMO;
import com.newland.property.po.purchase.PurchaseApplyPo;
import com.newland.property.store.bmo.purchase.IPurchaseApplyBMO;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("purchaseApplyBMOImpl")
public class PurchaseApplyBMOImpl implements IPurchaseApplyBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Override
    @PropertyTransactional
    public ResponseEntity<String> apply(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson) {
        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);
        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "采购申请失败");
        }
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        if(!purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT) && !purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT)){
            purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
            purchaseApplyUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功");
    }
}
