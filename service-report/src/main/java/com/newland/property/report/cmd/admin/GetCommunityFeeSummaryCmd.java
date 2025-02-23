package com.newland.property.report.cmd.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.report.QueryStatisticsDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.report.statistics.IBaseDataStatistics;
import com.newland.property.report.statistics.IFeeStatistics;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "admin.getCommunityFeeSummary")
public class GetCommunityFeeSummaryCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");

        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_ADMIN);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        if(reqJson.containsKey("endDate") && !reqJson.getString("endDate").contains(":")) {
            queryStatisticsDto.setEndDate(reqJson.getString("endDate") + " 23:59:59");
        }
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));

        if(reqJson.containsKey("configIds")){
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }

        //todo 查询历史欠费
        double hisOweFee = feeStatisticsImpl.getHisMonthOweFee(queryStatisticsDto);

        //todo 查询 单月欠费
        double curOweFee = feeStatisticsImpl.getCurMonthOweFee(queryStatisticsDto);

        //todo 查询当月应收
        double curReceivableFee = feeStatisticsImpl.getCurReceivableFee(queryStatisticsDto);

        //todo 查询 欠费追回
        double hisReceivedFee = feeStatisticsImpl.getHisReceivedFee(queryStatisticsDto);

        //todo  查询 预交费用
        double preReceivedFee = feeStatisticsImpl.getPreReceivedFee(queryStatisticsDto);

        //todo 查询实收
        double receivedFee = feeStatisticsImpl.getReceivedFee(queryStatisticsDto);

        //todo 房屋数
        long roomCount = baseDataStatisticsImpl.getRoomCount(queryStatisticsDto);

        //todo 收费房屋数
        long feeRoomCount = feeStatisticsImpl.getFeeRoomCount(queryStatisticsDto);

        //todo 欠费户数
        int oweRoomCount = feeStatisticsImpl.getOweRoomCount(queryStatisticsDto);

        JSONObject data = new JSONObject();
        data.put("hisOweFee", hisOweFee);
        data.put("curOweFee", curOweFee);
        data.put("hisReceivedFee", hisReceivedFee);
        data.put("preReceivedFee", preReceivedFee);
        data.put("receivedFee", receivedFee);
        data.put("roomCount", roomCount);
        data.put("feeRoomCount", feeRoomCount);
        data.put("oweRoomCount", oweRoomCount);
        data.put("curReceivableFee", curReceivableFee);

        JSONArray datas = new JSONArray();
        datas.add(data);
        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
