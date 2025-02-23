package com.newland.property.acct.bmo.shopVipAccountDetail.impl;

import com.newland.property.acct.bmo.shopVipAccountDetail.IGetShopVipAccountDetailBMO;
import com.newland.property.dto.shop.ShopVipAccountDetailDto;
import com.newland.property.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getShopVipAccountDetailBMOImpl")
public class GetShopVipAccountDetailBMOImpl implements IGetShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;



    /**
     * @param shopVipAccountDetailDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ShopVipAccountDetailDto shopVipAccountDetailDto) {


        int count = shopVipAccountDetailInnerServiceSMOImpl.queryShopVipAccountDetailsCount(shopVipAccountDetailDto);

        List<ShopVipAccountDetailDto> shopVipAccountDetailDtos = null;
        if (count > 0) {
            shopVipAccountDetailDtos = shopVipAccountDetailInnerServiceSMOImpl.queryShopVipAccountDetails(shopVipAccountDetailDto);
        } else {
            shopVipAccountDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) shopVipAccountDetailDto.getRow()), count, shopVipAccountDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


}
