package com.newland.property.community.cmd.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.intf.community.IParkingAreaAttrV1InnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingAreaPo;
import com.newland.property.po.parking.ParkingAreaAttrPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "parkingArea.saveParkingArea")
public class SaveParkingAreaCmd extends Cmd {

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaAttrV1InnerServiceSMO parkingAreaAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "num", "必填，请填写停车场编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择停车场类型");
        //属性校验
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        addParkingArea(reqJson);
        dealAttr(reqJson);
    }

    public void addParkingArea(JSONObject paramInJson) {
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setNum(paramInJson.getString("num"));
        parkingAreaDto.setCommunityId(paramInJson.getString("communityId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listIsNull(parkingAreaDtos, "停车场编号重复，请重新添加！");
        JSONObject businessParkingArea = new JSONObject();
        businessParkingArea.putAll(paramInJson);
        businessParkingArea.put("paId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_paId));
        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(businessParkingArea, ParkingAreaPo.class);
        //super.insert(dataFlowContext, parkingAreaPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA);
        int flag = parkingAreaV1InnerServiceSMOImpl.saveParkingArea(parkingAreaPo);

        if (flag < 1) {
            throw new CmdException("保存停车场失败");
        }
        paramInJson.put("paId", businessParkingArea.getString("paId"));
    }

    private void dealAttr(JSONObject paramObj) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }

        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("paId", paramObj.getString("paId"));
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            // parkingAreaAttrBMOImpl.addParkingAreaAttr(attr, context);
            ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(attr, ParkingAreaAttrPo.class);
            flag = parkingAreaAttrV1InnerServiceSMOImpl.saveParkingAreaAttr(parkingAreaAttrPo);
            if (flag < 1) {
                throw new CmdException("保存停车场失败");
            }
        }

    }
}
