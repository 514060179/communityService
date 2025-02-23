package com.newland.property.store.bmo.collection.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.purchase.PurchaseApplyDto;
import com.newland.property.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.newland.property.intf.store.IPurchaseApplyInnerServiceSMO;
import com.newland.property.po.purchase.PurchaseApplyPo;
import com.newland.property.store.bmo.collection.IGoodsCollectionBMO;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("goodsCollectionBMOImpl")
public class GoodsCollectionBMOImpl implements IGoodsCollectionBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Override
    @PropertyTransactional
    public ResponseEntity<String> collection(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson) {

        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);

        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "物品领用申请失败");
        }

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
        if (!PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT.equals(purchaseApplyPo.getWarehousingWay())) {
            goodCollectionUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "物品领用成功");
    }
}
