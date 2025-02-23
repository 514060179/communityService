/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.job.adapt.hcIot.tempCarFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.fee.TempCarFeeConfigAttrDto;
import com.newland.property.dto.fee.TempCarFeeConfigDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.newland.property.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.newland.property.po.tempCarFee.TempCarFeeConfigPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 车辆同步适配器
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "modifyTempCarFeeConfigToIotAdapt")
public class ModifyTempCarFeeConfigToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcTempCarFeeConfigAsynImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;


    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override

    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray  businessTempCarFeeConfigs = new JSONArray();
        if (data.containsKey(TempCarFeeConfigPo.class.getSimpleName())) {
            Object bObj = data.get(TempCarFeeConfigPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessTempCarFeeConfigs.add(bObj);
            } else if (bObj instanceof List) {
                businessTempCarFeeConfigs = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessTempCarFeeConfigs = (JSONArray) bObj;
            }
            //JSONObject businessTempCarFeeConfig = data.getJSONObject("businessTempCarFeeConfig");

        }else {
            if (data instanceof JSONObject) {
                businessTempCarFeeConfigs.add(data);
            }
        }
        for (int bTempCarFeeConfigIndex = 0; bTempCarFeeConfigIndex < businessTempCarFeeConfigs.size(); bTempCarFeeConfigIndex++) {
            JSONObject businessTempCarFeeConfig = businessTempCarFeeConfigs.getJSONObject(bTempCarFeeConfigIndex);
            doSendTempCarFeeConfig(business, businessTempCarFeeConfig);
        }
    }

    private void doSendTempCarFeeConfig(Business business, JSONObject businessTempCarFeeConfig) {

        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(businessTempCarFeeConfig, TempCarFeeConfigPo.class);

        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setConfigId(tempCarFeeConfigPo.getConfigId());
        tempCarFeeConfigDto.setCommunityId(tempCarFeeConfigPo.getCommunityId());
        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);

        Assert.listOnlyOne(tempCarFeeConfigDtos, "未找到临时车收费标准");

        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos = tempCarFeeConfigDtos.get(0).getTempCarFeeConfigAttrs();


        JSONObject postParameters = new JSONObject();

        postParameters.put("feeName", tempCarFeeConfigDtos.get(0).getFeeName());
        postParameters.put("carType", tempCarFeeConfigDtos.get(0).getCarType());
        postParameters.put("ruleId", tempCarFeeConfigDtos.get(0).getRuleId());
        postParameters.put("startTime", tempCarFeeConfigDtos.get(0).getStartTime());
        postParameters.put("endTime", tempCarFeeConfigDtos.get(0).getEndTime());
        postParameters.put("extPaId", tempCarFeeConfigDtos.get(0).getPaId());
        postParameters.put("extConfigId", tempCarFeeConfigDtos.get(0).getConfigId());
        postParameters.put("extCommunityId", tempCarFeeConfigDtos.get(0).getCommunityId());
        JSONArray attrs = new JSONArray();
        if (tempCarFeeConfigAttrDtos != null) {
            for (TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto : tempCarFeeConfigAttrDtos) {
                JSONObject attr = new JSONObject();
                attr.put("specCd", tempCarFeeConfigAttrDto.getSpecCd());
                attr.put("value", tempCarFeeConfigAttrDto.getValue());
                attrs.add(attr);
            }
        }
        postParameters.put("attrs", attrs);
        hcTempCarFeeConfigAsynImpl.updateTempCarFeeConfig(postParameters);
    }
}
