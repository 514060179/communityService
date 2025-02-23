package com.newland.property.fee.cmd.receipt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.fee.FeeReceiptDetailDto;
import com.newland.property.intf.fee.*;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 手工生成收据
 */
@NewlandPropertyCmd(serviceCode = "receipt.generatorReceipt")
public class GeneratorReceiptCmd extends Cmd {

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IGeneratorFeeReceiptInnerServiceSMO generatorFeeReceiptInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "detailId", "未包含收据ID");


        //todo 查询收费明细是否存在
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "缴费明细不存在");


        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailId(reqJson.getString("detailId"));
        feeReceiptDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);

        if (feeReceiptDetailDtos != null && feeReceiptDetailDtos.size() > 0) {
            throw new CmdException("收据已存在");
        }


        reqJson.put("feeDetailDto", feeDetailDtos.get(0));


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDetailDto feeDetailDto = (FeeDetailDto) reqJson.get("feeDetailDto");


        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(feeDetailDto, PayFeeDetailPo.class);

        String receiptCode = reqJson.getString("receiptCode");
        if (!StringUtil.isEmpty(receiptCode)) {
            CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode);
        }

        //todo 手工打印
        generatorFeeReceiptInnerServiceSMOImpl.generator(payFeeDetailPo);

        if (!StringUtil.isEmpty(receiptCode)) {
            CommonCache.removeValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE);
        }

        context.setResponseEntity(ResultVo.success());
    }
}
