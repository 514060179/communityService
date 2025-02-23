package com.newland.property.job.adapt.payment.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.smo.IComputeFeeSMO;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.newland.property.intf.fee.IFeeReceiptInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.fee.FeeReceiptPo;
import com.newland.property.po.fee.FeeReceiptDetailPo;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ExceptionUtil;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 退费审核同步收据
 *
 * @author fqz
 * @date 2023-03-09 16:28
 */
@Component(value = "updateReturnPayFeeAdapt")
public class UpdateReturnPayFeeAdapt extends DatabusAdaptImpl {

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(UpdateReturnPayFeeAdapt.class);

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
            //新增收据和收据明细
            doFeeReceipt(business, businessPayFeeDetail);
        }
    }

    //新增收据和收据明细
    private void doFeeReceipt(Business business, JSONObject paramInJson) {
        try {
            //查询缴费明细
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailPo.class);
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "未查询到费用信息");
            feeDto = feeDtos.get(0);
            //查询业主信息
            OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
            //添加收据
            FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
            feeReceiptPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
            feeReceiptPo.setCommunityId(paramInJson.getString("communityId"));
            feeReceiptPo.setObjType(paramInJson.getString("payerObjType")); //收据对象 3333 房屋 6666 车位车辆
            feeReceiptPo.setObjId(paramInJson.getString("payerObjId")); //对象ID
            feeReceiptPo.setObjName(paramInJson.getString("payerObjName")); //对象名称
            feeReceiptPo.setAmount(paramInJson.getString("receivedAmount")); //退费总金额
            feeReceiptPo.setRemark("退费收据");
            feeReceiptPo.setPayObjId(ownerDto.getOwnerId()); //付费人id
            feeReceiptPo.setPayObjName(ownerDto.getName()); //付费人姓名
            int i = feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);
            if (i < 1) {
                throw new CmdException("添加收据失败");
            }
            //添加收据详情
            FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
            feeReceiptDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            feeReceiptDetailPo.setReceiptId(feeReceiptPo.getReceiptId()); //收据id
            feeReceiptDetailPo.setFeeId(paramInJson.getString("feeId")); //费用id
            feeReceiptDetailPo.setFeeName(feeDtos.get(0).getFeeName());
            String roomArea = "";
            if (!StringUtil.isEmpty(feeDtos.get(0).getPayerObjType()) && "3333".equals(feeDtos.get(0).getPayerObjType())) { //房屋
                RoomDto roomDto = new RoomDto();
                roomDto.setRoomId(feeDtos.get(0).getPayerObjId());
                //查询房屋
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                Assert.listOnlyOne(roomDtos, "查询房屋错误！");
                roomArea = roomDtos.get(0).getRoomArea();
            }
            feeReceiptDetailPo.setArea(roomArea); //面积/用量
            feeReceiptDetailPo.setStartTime(paramInJson.getString("startTime"));
            feeReceiptDetailPo.setEndTime(paramInJson.getString("endTime"));
            feeReceiptDetailPo.setAmount(feeReceiptPo.getAmount());
            feeReceiptDetailPo.setCycle(paramInJson.getString("cycles"));
            feeReceiptDetailPo.setCommunityId(paramInJson.getString("communityId"));
            int flag = feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
            if (flag < 1) {
                throw new CmdException("添加收据详情失败");
            }
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
