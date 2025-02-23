package com.newland.property.acct.bmo.shopVipAccountDetail.impl;

import com.newland.property.acct.bmo.shopVipAccountDetail.IDeleteShopVipAccountDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.newland.property.po.shop.ShopVipAccountDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteShopVipAccountDetailBMOImpl")
public class DeleteShopVipAccountDetailBMOImpl implements IDeleteShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;

    /**
     * @param shopVipAccountDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ShopVipAccountDetailPo shopVipAccountDetailPo) {

        int flag = shopVipAccountDetailInnerServiceSMOImpl.deleteShopVipAccountDetail(shopVipAccountDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
