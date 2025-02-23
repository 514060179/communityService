package com.newland.property.job.adapt.payment.smartMeter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.fee.FeeAttrDto;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.meter.MeterMachineDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.common.IMeterMachineV1InnerServiceSMO;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ExceptionUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 智能水电表充值
 */
@Component(value = "smartMeterAdapt")
public class SmartMeterAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(SmartMeterAdapt.class);

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();

        if (data != null) {
            logger.debug("请求日志:{}", data);
        }
        JSONArray businessPayFeeDetails = null;
        if (data == null) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setbId(business.getbId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未查询到缴费记录");
            businessPayFeeDetails = JSONArray.parseArray(JSONArray.toJSONString(feeDetailDtos, SerializerFeature.WriteDateUseDateFormat));
        } else if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFeeDetails = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFeeDetails = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(data);
            }
        }

        if (businessPayFeeDetails == null) {
            return;
        }
        for (int bPayFeeDetailIndex = 0; bPayFeeDetailIndex < businessPayFeeDetails.size(); bPayFeeDetailIndex++) {
            JSONObject businessPayFeeDetail = businessPayFeeDetails.getJSONObject(bPayFeeDetailIndex);
            doPayFeeDetail(business, businessPayFeeDetail);
        }
    }

    private void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {
        try {
            //查询缴费明细
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessPayFeeDetail, PayFeeDetailPo.class);
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

            Assert.listOnlyOne(feeDtos, "未查询到费用信息");

            feeDto = feeDtos.get(0);


            //判断是否有智能水电表
            MeterMachineDto meterMachineDto = new MeterMachineDto();
            meterMachineDto.setFeeConfigId(feeDto.getConfigId());
            meterMachineDto.setRoomId(feeDto.getPayerObjId());
            meterMachineDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

            //没有智能电表
            if (meterMachineDtos == null || meterMachineDtos.size() < 1) {
                return;
            }

            String degree = FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION);

            meterMachineDtos.get(0).setRechargeDegree(Double.parseDouble(degree));
            meterMachineDtos.get(0).setRechargeMoney(Double.parseDouble(payFeeDetailPo.getReceivableAmount()));

            //智能电表充值
            meterMachineV1InnerServiceSMOImpl.reChargeMeterMachines(meterMachineDtos.get(0));

        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }
}
