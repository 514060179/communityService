package com.newland.property.acct.bmo.shopVipAccount.impl;

import com.newland.property.acct.bmo.shopVipAccount.ISaveShopVipAccountBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.acct.IShopVipAccountInnerServiceSMO;
import com.newland.property.po.shop.ShopVipAccountPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveShopVipAccountBMOImpl")
public class SaveShopVipAccountBMOImpl implements ISaveShopVipAccountBMO {

    @Autowired
    private IShopVipAccountInnerServiceSMO shopVipAccountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param shopVipAccountPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ShopVipAccountPo shopVipAccountPo) {

        shopVipAccountPo.setVipAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vipAcctId));
        int flag = shopVipAccountInnerServiceSMOImpl.saveShopVipAccount(shopVipAccountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
