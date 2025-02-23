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
package com.newland.property.fee.cmd.feeSub;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.payFeeSub.PayFeeSubDto;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeSubV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.payFeeSub.PayFeeSubPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：合并费用
 * 服务编码：payFeeSub.deletePayFeeSub
 * 请求路劲：/app/payFeeSub.DeletePayFeeSub
 * add by 吴学文 at 2024-01-05 12:10:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "feeSub.mergePayFee")
public class MergePayFeeCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(MergePayFeeCmd.class);

    @Autowired
    private IPayFeeSubV1InnerServiceSMO payFeeSubV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "feeId", "feeId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

        //todo 检查费用是否存在

        PayFeeSubDto payFeeSubDto = new PayFeeSubDto();
        payFeeSubDto.setFeeId(reqJson.getString("feeId"));
        payFeeSubDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeSubDto> payFeeSubDtos = payFeeSubV1InnerServiceSMOImpl.queryPayFeeSubs(payFeeSubDto);

        if (ListUtil.isNull(payFeeSubDtos)) {
            throw new CmdException("没有需要合并的费用");
        }

        payFeeSubDto = new PayFeeSubDto();
        payFeeSubDto.setPreFeeId(payFeeSubDtos.get(0).getPreFeeId());
        payFeeSubDto.setCommunityId(reqJson.getString("communityId"));
        payFeeSubDtos = payFeeSubV1InnerServiceSMOImpl.queryPayFeeSubs(payFeeSubDto);

        if (ListUtil.isNull(payFeeSubDtos)) {
            throw new CmdException("没有需要合并的费用");
        }

        //todo 拆分费用是否存在缴费记录，如果存在缴费记录，不能合并

        for (PayFeeSubDto tmpPayFeeSubDto : payFeeSubDtos) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setState(FeeDetailDto.STATE_NORMAL);
            feeDetailDto.setFeeId(tmpPayFeeSubDto.getFeeId());
            feeDetailDto.setCommunityId(tmpPayFeeSubDto.getCommunityId());
            int count = feeDetailInnerServiceSMOImpl.queryFeeDetailsCount(feeDetailDto);
            if (count > 0) {
                throw new CmdException("拆分后费用存在缴费记录 不能合并处理");
            }
        }

        reqJson.put("payFeeSubDtos",payFeeSubDtos);
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        List<PayFeeSubDto> payFeeSubDtos = (List<PayFeeSubDto>)reqJson.get("payFeeSubDtos");

        //todo 删除拆分费用
        PayFeeSubPo payFeeSubPo = new PayFeeSubPo();
        payFeeSubPo.setPreFeeId(payFeeSubDtos.get(0).getPreFeeId());
        payFeeSubPo.setCommunityId(payFeeSubDtos.get(0).getCommunityId());
        int flag =  payFeeSubV1InnerServiceSMOImpl.deletePayFeeSub(payFeeSubPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        //todo 结束拆分费用
        for (PayFeeSubDto tmpPayFeeSubDto : payFeeSubDtos) {
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(tmpPayFeeSubDto.getFeeId());
            payFeePo.setState(FeeDto.STATE_FINISH);
            payFeePo.setCommunityId(tmpPayFeeSubDto.getCommunityId());
             flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("结束费用失败");
            }
        }

        //todo 启用原费用
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeSubDtos.get(0).getPreFeeId());
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(payFeeSubDtos.get(0).getCommunityId());
        flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("启用原费用失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
