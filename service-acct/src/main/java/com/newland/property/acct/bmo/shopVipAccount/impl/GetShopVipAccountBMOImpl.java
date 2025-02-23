package com.newland.property.acct.bmo.shopVipAccount.impl;

import com.newland.property.acct.bmo.shopVipAccount.IGetShopVipAccountBMO;
import com.newland.property.dto.shop.ShopVipAccountDto;
import com.newland.property.intf.acct.IShopVipAccountInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getShopVipAccountBMOImpl")
public class GetShopVipAccountBMOImpl implements IGetShopVipAccountBMO {

    @Autowired
    private IShopVipAccountInnerServiceSMO shopVipAccountInnerServiceSMOImpl;

    /**
     * @param shopVipAccountDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ShopVipAccountDto shopVipAccountDto) {


        int count = shopVipAccountInnerServiceSMOImpl.queryShopVipAccountsCount(shopVipAccountDto);

        List<ShopVipAccountDto> shopVipAccountDtos = null;
        if (count > 0) {
            shopVipAccountDtos = shopVipAccountInnerServiceSMOImpl.queryShopVipAccounts(shopVipAccountDto);
        } else {
            shopVipAccountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) shopVipAccountDto.getRow()), count, shopVipAccountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
