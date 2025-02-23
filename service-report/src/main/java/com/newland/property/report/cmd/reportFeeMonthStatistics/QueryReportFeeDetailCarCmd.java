package com.newland.property.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.report.QueryStatisticsDto;
import com.newland.property.report.statistics.IBaseDataStatistics;
import com.newland.property.report.statistics.IFeeStatistics;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.MoneyUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询车辆费用明细表
 * 以房屋为维度统计 房屋的各个费用项 欠费和实收
 */

@NewlandPropertyCmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeDetailCar")
public class QueryReportFeeDetailCarCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        queryStatisticsDto.setPage(reqJson.getInteger("page"));
        queryStatisticsDto.setRow(reqJson.getInteger("row"));
        queryStatisticsDto.setStoreId(storeId);
        long count = baseDataStatisticsImpl.getCarCount(queryStatisticsDto);
        List<OwnerCarDto> ownerCarDtos = null;
        if (count > 0) {
            ownerCarDtos = baseDataStatisticsImpl.getCar(queryStatisticsDto);
        } else {
            ownerCarDtos = new ArrayList<>();
        }

        // todo 计算 房屋欠费实收数据
        JSONArray datas = computeCarOweReceivedFee(ownerCarDtos, queryStatisticsDto);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) queryStatisticsDto.getRow()), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 计算合同欠费 实收费用
     *
     * @param ownerCarDtos
     * @return
     */
    private JSONArray computeCarOweReceivedFee(List<OwnerCarDto> ownerCarDtos, QueryStatisticsDto queryStatisticsDto) {
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> objIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            objIds.add(ownerCarDto.getCarId());
            data = new JSONObject();
            data.put("carId", ownerCarDto.getCarId());
            data.put("carNum", ownerCarDto.getCarNum() );
            data.put("ownerName", ownerCarDto.getOwnerName());
            data.put("ownerId", ownerCarDto.getOwnerId());
            data.put("link", ownerCarDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setObjIds(objIds.toArray(new String[objIds.size()]));
        List<Map> infos = feeStatisticsImpl.getObjFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = null;
        BigDecimal receivedFee = null;
        double oweFeeD = 0;
        double receivedFeeD = 0;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            oweFee = new BigDecimal(0.00);
            receivedFee = new BigDecimal(0.00);
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("carId").toString().equals(info.get("objId"))) {
                    continue;
                }

                oweFeeD = Double.parseDouble(info.get("oweFee").toString());
                receivedFeeD = Double.parseDouble(info.get("receivedFee").toString());

                oweFee = oweFee.add(new BigDecimal(oweFeeD + ""));
                receivedFee = receivedFee.add(new BigDecimal(receivedFeeD + ""));
                data.put("oweFee" + info.get("feeTypeCd").toString(), MoneyUtil.computePriceScale(oweFeeD));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), MoneyUtil.computePriceScale(receivedFeeD));
            }
            data.put("oweFee", MoneyUtil.computePriceScale(oweFee.doubleValue()));
            data.put("receivedFee", MoneyUtil.computePriceScale(receivedFee.doubleValue()));
        }

        return datas;
    }
}
