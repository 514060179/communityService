package com.newland.property.fee.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.core.factory.CommunitySettingFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.smo.IComputeFeeSMO;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.fee.*;
import com.newland.property.po.fee.FeeReceiptDetailPo;
import com.newland.property.po.fee.FeeReceiptPo;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ExceptionUtil;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 收据内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GeneratorFeeReceiptInnerServiceSMOImpl extends BaseServiceSMO implements IGeneratorFeeReceiptInnerServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(GeneratorFeeReceiptInnerServiceSMOImpl.class);

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //键(退费收据开关)
    public static final String REFUND_RECEIPT_SWITCH = "REFUND_RECEIPT_SWITCH";

    @Override
    public int generator(@RequestBody PayFeeDetailPo payFeeDetailPo) {
        int saveFlag = 1;
        try {
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "未查询到费用信息");
            feeDto = feeDtos.get(0);
            //查询业主信息
            OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
            //获取小区配置里退费收据开关(open开;off关)
            String refundReceiptSwitch = CommunitySettingFactory.getValue(payFeeDetailPo.getCommunityId(), REFUND_RECEIPT_SWITCH);
            // if received amount lt zero
            if ("off".equals(refundReceiptSwitch) && Double.parseDouble(payFeeDetailPo.getReceivedAmount()) < 0) {
                return 0;
            }
            String receiptCode = CommonCache.getValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE);
            //todo 如果为空重新生成收据编号
            if (StringUtil.isEmpty(receiptCode)) {
                receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(payFeeDetailPo.getCommunityId());
            }
            //添加收据和收据详情
            FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
            FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
            feeReceiptDetailPo.setAmount(payFeeDetailPo.getReceivedAmount());
            feeReceiptDetailPo.setCommunityId(feeDto.getCommunityId());
            feeReceiptDetailPo.setCycle(payFeeDetailPo.getCycles());
            feeReceiptDetailPo.setDetailId(payFeeDetailPo.getDetailId());
            feeReceiptDetailPo.setEndTime(payFeeDetailPo.getEndTime());
            feeReceiptDetailPo.setFeeId(feeDto.getFeeId());
            feeReceiptDetailPo.setFeeName(StringUtil.isEmpty(feeDto.getImportFeeName()) ? feeDto.getFeeName() : feeDto.getImportFeeName());
            feeReceiptDetailPo.setStartTime(payFeeDetailPo.getStartTime());
            feeReceiptDetailPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
            feeReceiptDetailPo.setCreateTime(payFeeDetailPo.getCreateTime());
            //处理 小数点后 0
            feeDto.setSquarePrice(Double.parseDouble(feeDto.getSquarePrice()) + "");
            feeDto.setAdditionalAmount(Double.parseDouble(feeDto.getAdditionalAmount()) + "");
            computeFeeSMOImpl.freshFeeReceiptDetail(feeDto, feeReceiptDetailPo);
            feeReceiptPo.setAmount(feeReceiptDetailPo.getAmount()); //实收金额
            feeReceiptPo.setReceivableAmount(payFeeDetailPo.getReceivableAmount()); //应收金额
            feeReceiptPo.setCommunityId(feeReceiptDetailPo.getCommunityId());
            feeReceiptPo.setReceiptId(feeReceiptDetailPo.getReceiptId());
            feeReceiptPo.setObjType(feeDto.getPayerObjType());
            feeReceiptPo.setObjId(feeDto.getPayerObjId());
            feeReceiptPo.setObjName(computeFeeSMOImpl.getFeeObjName(feeDto));
            feeReceiptPo.setPayObjId(ownerDto.getOwnerId());
            feeReceiptPo.setPayObjName(ownerDto.getName());
            feeReceiptPo.setCreateTime(payFeeDetailPo.getCreateTime());
            feeReceiptPo.setReceiptCode(receiptCode);
            //这里只是写入 收据表，暂不考虑 事务一致性问题，就算写入失败 也只是影响 收据打印，如果 贵公司对 收据要求 比较高，不能有失败的情况 请加入事务管理
            feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
            feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);
        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
        return saveFlag;
    }
}
