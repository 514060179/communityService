package com.newland.property.acct.bmo.shopVipAccount;
import com.newland.property.dto.shop.ShopVipAccountDto;
import org.springframework.http.ResponseEntity;
public interface IGetShopVipAccountBMO {


    /**
     * 查询会员账户
     * add by wuxw
     * @param  shopVipAccountDto
     * @return
     */
    ResponseEntity<String> get(ShopVipAccountDto shopVipAccountDto);


}
