package com.newland.property.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.PropertyThreadPoolFactory;
import com.newland.property.dto.report.QueryStatisticsDto;
import com.newland.property.report.statistics.IBaseDataStatistics;
import com.newland.property.report.statistics.IFeeStatistics;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.MoneyUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查询费用汇总表
 * <p>
 * add by  wuxw
 */
@NewlandPropertyCmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeSummary")
public class QueryReportFeeSummaryCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    /**
     * 校验查询条件
     * <p>
     * 开始时间
     * 结束时间
     * 房屋
     * 业主
     * 楼栋
     * 费用项
     *
     * @param event   事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        /*queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));*/
        if (reqJson.containsKey("startDate") && !reqJson.getString("startDate").contains(":")) {
            queryStatisticsDto.setStartDate(reqJson.getString("startDate") + " 00:00:00");
        }
        if (reqJson.containsKey("endDate") && !reqJson.getString("endDate").contains(":")) {
            queryStatisticsDto.setEndDate(reqJson.getString("endDate") + " 23:59:59");
        }
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        if (reqJson.containsKey("configIds")) {
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }


        JSONObject data = new JSONObject();

        PropertyThreadPoolFactory propertyThreadPoolFactory = null;
        try {
            propertyThreadPoolFactory = PropertyThreadPoolFactory.getInstance().createThreadPool(5);
            propertyThreadPoolFactory.submit(() -> {
                //todo 查询历史欠费
                double hisOweFee = feeStatisticsImpl.getHisMonthOweFee(queryStatisticsDto);
                data.put("hisOweFee", MoneyUtil.computePriceScale(hisOweFee));
                return hisOweFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo 查询 单月欠费
                double curOweFee = feeStatisticsImpl.getCurMonthOweFee(queryStatisticsDto);
                data.put("curOweFee", MoneyUtil.computePriceScale(curOweFee));
                return curOweFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo 查询当月应收
                double curReceivableFee = feeStatisticsImpl.getCurReceivableFee(queryStatisticsDto);
                data.put("curReceivableFee", MoneyUtil.computePriceScale(curReceivableFee));
                return curReceivableFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo 查询 欠费追回
                double hisReceivedFee = feeStatisticsImpl.getHisReceivedFee(queryStatisticsDto);
                data.put("hisReceivedFee", MoneyUtil.computePriceScale(hisReceivedFee));
                return hisReceivedFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo  查询 预交费用
                double preReceivedFee = feeStatisticsImpl.getPreReceivedFee(queryStatisticsDto);
                data.put("preReceivedFee", MoneyUtil.computePriceScale(preReceivedFee));
                return preReceivedFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo 查询实收
                double receivedFee = feeStatisticsImpl.getReceivedFee(queryStatisticsDto);
                data.put("receivedFee", MoneyUtil.computePriceScale(receivedFee));
                return receivedFee;
            });

            propertyThreadPoolFactory.submit(() -> {
                //todo 房屋数
                long roomCount = baseDataStatisticsImpl.getRoomCount(queryStatisticsDto);
                data.put("roomCount", roomCount);
                return roomCount;
            });
            propertyThreadPoolFactory.submit(() -> {
                //todo 收费房屋数
                long feeRoomCount = feeStatisticsImpl.getFeeRoomCount(queryStatisticsDto);
                data.put("feeRoomCount", feeRoomCount);
                return feeRoomCount;
            });
            propertyThreadPoolFactory.submit(() -> {
                //todo 欠费户数
                int oweRoomCount = feeStatisticsImpl.getOweRoomCount(queryStatisticsDto);
                data.put("oweRoomCount", oweRoomCount);
                return oweRoomCount;
            });

            propertyThreadPoolFactory.get();
        } finally {
            if (propertyThreadPoolFactory != null) {
                propertyThreadPoolFactory.stop();
            }
        }
        JSONArray datas = new JSONArray();
        datas.add(data);
        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
