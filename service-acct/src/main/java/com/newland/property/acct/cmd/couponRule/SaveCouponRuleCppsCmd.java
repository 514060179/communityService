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
package com.newland.property.acct.cmd.couponRule;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.coupon.CouponRuleCppsDto;
import com.newland.property.intf.acct.ICouponRuleCppsV1InnerServiceSMO;
import com.newland.property.po.coupon.CouponRuleCppsPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：couponRuleCpps.saveCouponRuleCpps
 * 请求路劲：/app/couponRuleCpps.SaveCouponRuleCpps
 * add by 吴学文 at 2022-11-22 01:37:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "couponRule.saveCouponRuleCpps")
public class SaveCouponRuleCppsCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveCouponRuleCppsCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICouponRuleCppsV1InnerServiceSMO couponRuleCppsV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "cppId", "请求报文中未包含cppId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "quantity", "请求报文中未包含quantity");

        //查询 规则是否已经关联过 优惠券

        CouponRuleCppsDto couponRuleCppsDto = new CouponRuleCppsDto();
        couponRuleCppsDto.setRuleId(reqJson.getString("ruleId"));
        couponRuleCppsDto.setCppId(reqJson.getString("cppId"));
        int count = couponRuleCppsV1InnerServiceSMOImpl.queryCouponRuleCppssCount(couponRuleCppsDto);

        if(count > 0){
            throw new CmdException("该优惠券已经关联过");
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CouponRuleCppsPo couponRuleCppsPo = BeanConvertUtil.covertBean(reqJson, CouponRuleCppsPo.class);
        couponRuleCppsPo.setCrcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = couponRuleCppsV1InnerServiceSMOImpl.saveCouponRuleCpps(couponRuleCppsPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
