/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.acct.cmd.parkingCoupon;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.parking.ParkingCouponCarDto;
import com.newland.property.dto.parking.ParkingCouponShopDto;
import com.newland.property.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.newland.property.intf.acct.IParkingCouponShopV1InnerServiceSMO;
import com.newland.property.intf.acct.IParkingCouponV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingCouponCarPo;
import com.newland.property.po.parking.ParkingCouponShopPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.lock.DistributedLock;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@NewlandPropertyCmdDoc(title = "商家赠送停车劵",
        description = "商家通过此接口赠送停车劵",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/parkingCoupon.saveParkingCouponCar",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "parkingCoupon.saveParkingCouponCar"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "couponShopId", length = 30, remark = "优惠劵ID"),
        @NewlandPropertyParamDoc(name = "giveWay", length = 30, remark = "赠送方式 1001 扫码获取 2002 商家添加 3003 购物自动赠送"),
        @NewlandPropertyParamDoc(name = "carNum", length = 30, remark = "车牌号"),
        @NewlandPropertyParamDoc(name = "shopId", length = 30, remark = "店铺ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),

        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\"shopId\":\"502022101140520018\",\"giveWay\":\"2002\",\"carNum\":\"青A88888\",\"couponShopId\":\"102022101112890007\"}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)

/**
 * 类表述：保存
 * 服务编码：parkingCouponCar.saveParkingCouponCar
 * 请求路劲：/app/parkingCouponCar.SaveParkingCouponCar
 * add by 吴学文 at 2022-10-12 13:02:09 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "parkingCoupon.saveParkingCouponCar")
public class SaveParkingCouponCarCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveParkingCouponCarCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponV1InnerServiceSMO parkingCouponV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponShopV1InnerServiceSMO parkingCouponShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "couponShopId", "请求报文中未包含couponShopId");
        Assert.hasKeyAndValue(reqJson, "shopId", "请求报文中未包含shopId");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "giveWay", "请求报文中未包含giveWay");
        Assert.hasKeyAndValue(reqJson, "code", "请求报文中未包含临时票据");

        String codeKey = reqJson.getString("shopId") + reqJson.getString("code");

        String checkCode = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,"CHECK_PARKING_COUPON_QRCODE_CODE");

        if ("OFF".equals(checkCode)) {
            return;
        }
        if (!reqJson.getString("code").equals(CommonCache.getAndRemoveValue(codeKey))) {
            throw new CmdException("非法操作");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        ParkingCouponShopDto parkingCouponShopDto = new ParkingCouponShopDto();
        parkingCouponShopDto.setCouponShopId(reqJson.getString("couponShopId"));
        parkingCouponShopDto.setShopId(reqJson.getString("shopId"));
        List<ParkingCouponShopDto> parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);

        Assert.listOnlyOne(parkingCouponShopDtos, "停车劵不存在");

        int quantity = Integer.parseInt(parkingCouponShopDtos.get(0).getQuantity());

        if (quantity < 1) {
            throw new CmdException("停车劵不足，请购买");
        }
        int flag = 0;
        // 这里加全局锁 防止 并发
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("couponShopId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            parkingCouponShopDto = new ParkingCouponShopDto();
            parkingCouponShopDto.setCouponShopId(reqJson.getString("couponShopId"));
            parkingCouponShopDto.setShopId(reqJson.getString("shopId"));
            parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);
            quantity = Integer.parseInt(parkingCouponShopDtos.get(0).getQuantity());
            if (quantity < 1) {
                throw new CmdException("停车劵不足，请购买");
            }
            ParkingCouponShopPo parkingCouponShopPo = new ParkingCouponShopPo();
            parkingCouponShopPo.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
            parkingCouponShopPo.setQuantity((quantity - 1) + "");
            flag = parkingCouponShopV1InnerServiceSMOImpl.updateParkingCouponShop(parkingCouponShopPo);
            if (flag < 1) {
                throw new CmdException("优惠券递减失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(key,requestId);
        }

        ParkingCouponCarPo parkingCouponCarPo = BeanConvertUtil.covertBean(reqJson, ParkingCouponCarPo.class);
        parkingCouponCarPo.setPccId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        parkingCouponCarPo.setCouponId(parkingCouponShopDtos.get(0).getCouponId());
        parkingCouponCarPo.setCommunityId(parkingCouponShopDtos.get(0).getCommunityId());
        parkingCouponCarPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        parkingCouponCarPo.setEndTime(DateUtil.getAddDayString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A, 1));
        parkingCouponCarPo.setPaId(parkingCouponShopDtos.get(0).getPaId());
        parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_WAIT);
        parkingCouponCarPo.setTypeCd(parkingCouponShopDtos.get(0).getTypeCd());
        parkingCouponCarPo.setValue(parkingCouponShopDtos.get(0).getValue());

        flag = parkingCouponCarV1InnerServiceSMOImpl.saveParkingCouponCar(parkingCouponCarPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
