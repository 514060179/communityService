package com.newland.property.acct.bmo.shopVipAccountDetail;
import com.newland.property.dto.shop.ShopVipAccountDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetShopVipAccountDetailBMO {


    /**
     * 查询会员账户交易
     * add by wuxw
     * @param  shopVipAccountDetailDto
     * @return
     */
    ResponseEntity<String> get(ShopVipAccountDetailDto shopVipAccountDetailDto);



}
