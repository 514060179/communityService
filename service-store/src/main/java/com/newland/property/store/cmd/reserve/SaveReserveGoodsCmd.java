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
package com.newland.property.store.cmd.reserve;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IReserveGoodsDetailV1InnerServiceSMO;
import com.newland.property.intf.store.IReserveGoodsV1InnerServiceSMO;
import com.newland.property.po.reserve.ReserveGoodsPo;
import com.newland.property.po.reserve.ReserveGoodsDetailPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：reserveGoods.saveReserveGoods
 * 请求路劲：/app/reserveGoods.SaveReserveGoods
 * add by 吴学文 at 2022-12-05 18:25:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "reserve.saveReserveGoods")
public class SaveReserveGoodsCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReserveGoodsCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReserveGoodsV1InnerServiceSMO reserveGoodsV1InnerServiceSMOImpl;


    @Autowired
    private IReserveGoodsDetailV1InnerServiceSMO reserveGoodsDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "catalogId", "请求报文中未包含catalogId");
        Assert.hasKeyAndValue(reqJson, "goodsName", "请求报文中未包含goodsName");
        Assert.hasKeyAndValue(reqJson, "goodsDesc", "请求报文中未包含goodsDesc");
        Assert.hasKeyAndValue(reqJson, "type", "请求报文中未包含type");
        Assert.hasKeyAndValue(reqJson, "paramsId", "请求报文中未包含paramsId");
        Assert.hasKeyAndValue(reqJson, "price", "请求报文中未包含price");
        Assert.hasKeyAndValue(reqJson, "startDate", "请求报文中未包含startDate");
        Assert.hasKeyAndValue(reqJson, "endDate", "请求报文中未包含endDate");
        Assert.hasKeyAndValue(reqJson, "imgUrl", "请求报文中未包含imgUrl");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReserveGoodsPo reserveGoodsPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsPo.class);
        reserveGoodsPo.setGoodsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = reserveGoodsV1InnerServiceSMOImpl.saveReserveGoods(reserveGoodsPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        ReserveGoodsDetailPo reserveGoodsDetailPo = new ReserveGoodsDetailPo();
        reserveGoodsDetailPo.setGoodsId(reserveGoodsPo.getGoodsId());
        reserveGoodsDetailPo.setCommunityId(reserveGoodsPo.getCommunityId());
        reserveGoodsDetailPo.setContent(reqJson.getString("content"));
        reserveGoodsDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        flag = reserveGoodsDetailV1InnerServiceSMOImpl.saveReserveGoodsDetail(reserveGoodsDetailPo);
        if (flag < 1) {
            throw new CmdException("保存详情失败");
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
