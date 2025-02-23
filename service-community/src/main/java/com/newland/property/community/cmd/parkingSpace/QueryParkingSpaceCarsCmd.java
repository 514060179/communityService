package com.newland.property.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.owner.ApiOwnerCarDataVo;
import com.newland.property.vo.api.owner.ApiOwnerCarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "parkingSpace.queryParkingSpaceCars")
public class QueryParkingSpaceCarsCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        refreshReqJson(reqJson);

        //根据车牌号去查询 车位信息
        queryParkingSpaceCar(reqJson, context);
    }

    /**
     * 根据车牌号 查询 停车位
     *
     * @param reqJson         请求报文
     * @param dataFlowContext 上线文对象
     */
    private void queryParkingSpaceCar(JSONObject reqJson,ICmdDataFlowContext dataFlowContext) {


        ApiOwnerCarVo apiOwnerCarVo = new ApiOwnerCarVo();

        int row = reqJson.getInteger("row");
        //查询总记录数
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        apiOwnerCarVo.setTotal(total);
        if (total > 0) {
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            apiOwnerCarVo.setOwnerCars(BeanConvertUtil.covertBeanList(ownerCarDtos, ApiOwnerCarDataVo.class));
        }

        apiOwnerCarVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerCarVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 获取 停车位Ids
     *
     * @param ownerCarDtos 业主车位
     * @return 停车位Ids
     */
    private String[] getPsIds(List<OwnerCarDto> ownerCarDtos) {
        List<String> psIds = new ArrayList<String>();
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            psIds.add(ownerCarDto.getPsId());
        }

        return psIds.toArray(new String[psIds.size()]);
    }

    /**
     * 请求数据处理
     *
     * @param reqJson 请求数据对象
     */
    private void refreshReqJson(JSONObject reqJson) {

        if (!reqJson.containsKey("state")) {
            return;
        }

        if ("SH".equals(reqJson.getString("state"))) {
            reqJson.put("states", new String[]{"S", "H"});
            reqJson.remove("state");
        }
    }
}
