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
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.parking.ParkingCouponCarDto;
import com.newland.property.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@NewlandPropertyCmdDoc(title = "查询商户赠送停车劵",
        description = "根据商户ID 查询商户赠送停车劵",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/parkingCoupon.listParkingCouponCar",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "parkingCoupon.listParkingCouponCar"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "page",type = "int",length = 11, remark = "页数"),
        @NewlandPropertyParamDoc(name = "row",type = "int", length = 11, remark = "行数"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "shopId", length = 30, remark = "商铺ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "couponName", type = "String", remark = "优惠券名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "startTime", type = "String", remark = "开始时间"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "endTime", type = "String", remark = "结束时间"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "paName", type = "String", remark = "停车场名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "paId", type = "String", remark = "停车场ID"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "stateName", type = "String", remark = "状态"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "shopName", type = "String", remark = "商家名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "carNum", type = "String", remark = "车牌号"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/parkingCoupon.listParkingCouponCar?page=1&row=100&communityId=2022081539020475&shopId=502022101140520018",
        resBody="{\"code\":0,\"data\":[{\"carNum\":\"青A88888\",\"communityId\":\"2022081539020475\",\"couponId\":\"102022101024650009\",\"couponName\":\"时卡优惠券\",\"couponShopId\":\"102022101112890007\",\"createTime\":\"2022-10-15 18:41:33\",\"endTime\":\"2022-10-16 18:41:32\",\"giveWay\":\"2002\",\"paId\":\"102022082382290048\",\"paName\":\"\",\"page\":-1,\"pccId\":\"102022101586160004\",\"records\":0,\"row\":0,\"shopId\":\"502022101140520018\",\"shopName\":\"测试商家\",\"startTime\":\"2022-10-15 18:41:32\",\"state\":\"1001\",\"stateName\":\"未使用\",\"statusCd\":\"0\",\"total\":0,\"typeCd\":\"1001\",\"value\":\"3\"}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)
/**
 * 类表述：查询
 * 服务编码：parkingCouponCar.listParkingCouponCar
 * 请求路劲：/app/parkingCouponCar.ListParkingCouponCar
 * add by 吴学文 at 2022-10-12 13:02:09 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "parkingCoupon.listParkingCouponCar")
public class ListParkingCouponCarCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListParkingCouponCarCmd.class);
    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ParkingCouponCarDto parkingCouponCarDto = BeanConvertUtil.covertBean(reqJson, ParkingCouponCarDto.class);

        int count = parkingCouponCarV1InnerServiceSMOImpl.queryParkingCouponCarsCount(parkingCouponCarDto);

        List<ParkingCouponCarDto> parkingCouponCarDtos = null;

        if (count > 0) {
            parkingCouponCarDtos = parkingCouponCarV1InnerServiceSMOImpl.queryParkingCouponCars(parkingCouponCarDto);
        } else {
            parkingCouponCarDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, parkingCouponCarDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
