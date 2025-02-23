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
package com.newland.property.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.coupon.CouponPropertyPoolDto;
import com.newland.property.intf.acct.ICouponPropertyPoolConfigV1InnerServiceSMO;
import com.newland.property.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.po.coupon.CouponPropertyPoolPo;
import com.newland.property.po.coupon.CouponPropertyPoolConfigPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：couponProperty.saveCouponPropertyPool
 * 请求路劲：/app/couponProperty.SaveCouponPropertyPool
 * add by 吴学文 at 2022-11-19 23:00:42 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "couponProperty.saveCouponPropertyPool")
public class SaveCouponPropertyPoolCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveCouponPropertyPoolCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "couponName", "请求报文中未包含couponName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "toType", "请求报文中未包含toType");
        Assert.hasKeyAndValue(reqJson, "stock", "请求报文中未包含stock");
        Assert.hasKeyAndValue(reqJson, "validityDay", "请求报文中未包含validityDay");

        if(!reqJson.containsKey("toTypes")){
            throw new CmdException("未包含用途");
        }

        JSONArray toTypes = reqJson.getJSONArray("toTypes");

        if(toTypes == null || toTypes.size()< 1){
            throw new CmdException("未包含用途");
        }

        JSONObject typeObj = null;
        for(int typeIndex = 0;typeIndex < toTypes.size(); typeIndex++){
            typeObj = toTypes.getJSONObject(typeIndex);
            if(!typeObj.containsKey("columnValue")){
                throw new CmdException(typeObj.getString("name")+"未填写值");
            }

            if(StringUtil.isEmpty(typeObj.getString("columnValue"))){
                throw new CmdException(typeObj.getString("name")+"未填写值");
            }
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos,"小区不存在");

        CouponPropertyPoolPo couponPropertyPoolPo = BeanConvertUtil.covertBean(reqJson, CouponPropertyPoolPo.class);
        couponPropertyPoolPo.setCppId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        couponPropertyPoolPo.setCommunityName(communityDtos.get(0).getName());
        couponPropertyPoolPo.setFromType(CouponPropertyPoolDto.FROM_TYPE_CUSTOM);
        couponPropertyPoolPo.setFromId(couponPropertyPoolPo.getCppId());
        couponPropertyPoolPo.setState(CouponPropertyPoolDto.STATE_Y);
        int flag = couponPropertyPoolV1InnerServiceSMOImpl.saveCouponPropertyPool(couponPropertyPoolPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray toTypes = reqJson.getJSONArray("toTypes");

        JSONObject typeObj = null;
        CouponPropertyPoolConfigPo couponPropertyPoolConfigPo = null;
        for(int typeIndex = 0;typeIndex < toTypes.size(); typeIndex++){
            typeObj = toTypes.getJSONObject(typeIndex);
            couponPropertyPoolConfigPo = new CouponPropertyPoolConfigPo();
            couponPropertyPoolConfigPo.setColumnKey(typeObj.getString("columnKey"));
            couponPropertyPoolConfigPo.setColumnValue(typeObj.getString("columnValue"));
            couponPropertyPoolConfigPo.setConfigId(GenerateCodeFactory.getGeneratorId("11"));
            couponPropertyPoolConfigPo.setCouponId(couponPropertyPoolPo.getCppId());
            couponPropertyPoolConfigV1InnerServiceSMOImpl.saveCouponPropertyPoolConfig(couponPropertyPoolConfigPo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
