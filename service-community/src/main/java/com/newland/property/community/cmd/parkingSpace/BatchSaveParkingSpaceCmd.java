package com.newland.property.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingSpacePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "parkingSpace.batchSaveParkingSpace")
public class BatchSaveParkingSpaceCmd extends Cmd {

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "startNum", "请求报文中未包含开始编号");
        Assert.jsonObjectHaveKey(reqJson, "endNum", "请求报文中未包含结束编号");
        Assert.jsonObjectHaveKey(reqJson, "paId", "请求报文中未包含停车场信息");
        Assert.jsonObjectHaveKey(reqJson, "parkingType", "请求报文中未包含车位类型");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        List<ParkingSpacePo> parkingSpacePos = new ArrayList<>();

        ParkingSpacePo parkingSpacePo = null;
        Calendar createTime = Calendar.getInstance();
        for (int i = reqJson.getIntValue("startNum"); i <= reqJson.getIntValue("endNum"); i++) {
            createTime.add(Calendar.SECOND,1);
            parkingSpacePo = BeanConvertUtil.covertBean(reqJson, ParkingSpacePo.class);
            parkingSpacePo.setNum(reqJson.getString("preNum") + i);
            parkingSpacePo.setState("F");
            parkingSpacePo.setPsId(GenerateCodeFactory.getPsId(GenerateCodeFactory.CODE_PREFIX_psId));
            parkingSpacePo.setbId("-1");
            parkingSpacePo.setCreateTime(DateUtil.getFormatTimeString(createTime.getTime(),DateUtil.DATE_FORMATE_STRING_A));
            parkingSpacePos.add(parkingSpacePo);
        }

        if (parkingSpacePos.size() < 1) {
            throw new CmdException("未包含添加车位");
        }


        int flag = parkingSpaceV1InnerServiceSMOImpl.saveParkingSpaces(parkingSpacePos);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }
}
