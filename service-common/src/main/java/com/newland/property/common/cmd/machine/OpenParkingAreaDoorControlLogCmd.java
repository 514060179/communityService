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
package com.newland.property.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.api.IApiCallBackInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：machine.openParkingAreaDoorControlLog
 * 请求路劲：/app/machine.openParkingAreaDoorControlLog
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "machine.openParkingAreaDoorControlLog")
public class OpenParkingAreaDoorControlLogCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(OpenParkingAreaDoorControlLogCmd.class);

    @Autowired
    private IApiCallBackInnerServiceSMO apiCallBackInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

//        try {
//            KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_API_SEND_PARKING_AREA_WEB, reqJson.toJSONString());
//        } catch (Exception e) {
//            logger.error("发送停车场信息失败", e);
//        }
        apiCallBackInnerServiceSMOImpl.webSentParkingArea(reqJson);
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
