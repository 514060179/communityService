package com.newland.property.acct.bmo.shopVipAccount.impl;

import com.newland.property.acct.bmo.shopVipAccount.IDeleteShopVipAccountBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.IShopVipAccountInnerServiceSMO;
import com.newland.property.po.shop.ShopVipAccountPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteShopVipAccountBMOImpl")
public class DeleteShopVipAccountBMOImpl implements IDeleteShopVipAccountBMO {

    @Autowired
    private IShopVipAccountInnerServiceSMO shopVipAccountInnerServiceSMOImpl;

    /**
     * @param shopVipAccountPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ShopVipAccountPo shopVipAccountPo) {

        int flag = shopVipAccountInnerServiceSMOImpl.deleteShopVipAccount(shopVipAccountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
