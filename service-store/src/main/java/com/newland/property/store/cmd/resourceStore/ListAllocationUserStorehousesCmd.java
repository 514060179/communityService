package com.newland.property.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.purchase.AllocationUserStorehouseDto;
import com.newland.property.intf.store.IAllocationUserStorehouseInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@NewlandPropertyCmd(serviceCode = "resourceStore.listAllocationUserStorehouses")
public class ListAllocationUserStorehousesCmd extends Cmd {

    @Autowired
    private IAllocationUserStorehouseInnerServiceSMO allocationUserStorehouseInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        AllocationUserStorehouseDto allocationUserStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationUserStorehouseDto.class);
        allocationUserStorehouseDto.setStoreId(storeId);

        int count = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehousesCount(allocationUserStorehouseDto);

        List<AllocationUserStorehouseDto> allocationUserStorehouseDtos = new ArrayList<>();

        if (count > 0) {
            List<AllocationUserStorehouseDto> allocationUserStorehouseList = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehouses(allocationUserStorehouseDto);
            //转赠总数量(小计)
            BigDecimal subTotalQuantity = BigDecimal.ZERO;
            //转赠总数量(小计)
            BigDecimal totalQuantity = BigDecimal.ZERO;
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouseList) {
                //获取转赠数量
                String giveQuantity = allocationUserStorehouse.getGiveQuantity();
                BigDecimal quantity = new BigDecimal(giveQuantity);
                //计算转赠总数量(小计)
                subTotalQuantity = subTotalQuantity.add(quantity);
            }
            //查询所有转赠记录
            allocationUserStorehouseDto.setPage(PageDto.DEFAULT_PAGE);
            List<AllocationUserStorehouseDto> allocationUserStorehouses = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehouses(allocationUserStorehouseDto);
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouses) {
                //获取转赠数量
                String giveQuantity = allocationUserStorehouse.getGiveQuantity();
                BigDecimal quantity = new BigDecimal(giveQuantity);
                //计算转赠总数量(大计)
                totalQuantity = totalQuantity.add(quantity);
            }
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouseList) {
                allocationUserStorehouse.setSubTotalQuantity(subTotalQuantity.toString());
                allocationUserStorehouse.setHighTotalQuantity(totalQuantity.toString());
                allocationUserStorehouseDtos.add(allocationUserStorehouse);
            }
        } else {
            allocationUserStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationUserStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
