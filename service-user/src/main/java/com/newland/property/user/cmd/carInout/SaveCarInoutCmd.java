package com.newland.property.user.cmd.carInout;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.ICarInoutV1InnerServiceSMO;
import com.newland.property.po.car.CarInoutPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

//@NewlandPropertyCmd(serviceCode = "carInout.saveCarInout")
public class SaveCarInoutCmd extends Cmd {

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写车辆状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "inTime", "必填，请选择进场时间");
        Assert.hasKeyAndValue(reqJson, "outTime", "必填，请选择出场时间");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        reqJson.put("inoutId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inoutId));
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(reqJson, CarInoutPo.class);

        int flag = carInoutV1InnerServiceSMOImpl.saveCarInout(carInoutPo);

        if(flag < 1){
            throw new CmdException("车辆进出失败");
        }
    }
}
