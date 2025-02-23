package com.newland.property.acct.bmo.shopVipAccountDetail.impl;

import com.newland.property.acct.bmo.shopVipAccountDetail.ISaveShopVipAccountDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.newland.property.po.shop.ShopVipAccountDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveShopVipAccountDetailBMOImpl")
public class SaveShopVipAccountDetailBMOImpl implements ISaveShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param shopVipAccountDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ShopVipAccountDetailPo shopVipAccountDetailPo) {

        shopVipAccountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = shopVipAccountDetailInnerServiceSMOImpl.saveShopVipAccountDetail(shopVipAccountDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
