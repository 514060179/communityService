package com.newland.property.acct.bmo.shopVipAccount;
import com.newland.property.po.shop.ShopVipAccountPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteShopVipAccountBMO {


    /**
     * 修改会员账户
     * add by wuxw
     * @param shopVipAccountPo
     * @return
     */
    ResponseEntity<String> delete(ShopVipAccountPo shopVipAccountPo);


}
