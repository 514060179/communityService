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
package com.newland.property.job.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.factory.WechatFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.dto.wechat.TemplateDataDto;
import com.newland.property.dto.wechat.SmallWechatAttrDto;
import com.newland.property.dto.fee.TempCarPayOrderDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.store.ISmallWeChatInnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.job.adapt.hcIot.GetToken;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @desc add by 吴学文 15:21
 */
public abstract class DatabusAdaptImpl implements IDatabusAdapt {
    private static Logger logger = LoggerFactory.getLogger(DatabusAdaptImpl.class);


    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    /**
     * 封装头信息
     *
     * @return
     */
    protected HttpHeaders getHeaders(RestTemplate outRestTemplate) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access_token", GetToken.get(outRestTemplate, false));
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        return httpHeaders;
    }


    /**
     * 开门
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo openDoor(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }
    /**
     * 开门
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo closeDoor(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    @Override
    public ResultVo getQRcode(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    @Override
    public ResultVo customCarInOut(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    @Override
    public ResultVo payVideo(MachineDto machineDto) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    @Override
    public ResultVo heartbeatVideo(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }


    /**
     * 重启设备
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResultVo restartMachine(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 失败重新送IOT
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResultVo reSendToIot(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 查询 临时车待支付订单
     *
     * @param tempCarPayOrderDto
     * @return
     */
    @Override
    public ResultVo getTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }
    /**
     * 修改在场车辆车牌号
     *
     * @param carInoutDto
     * @return
     */
    @Override
    public ResultVo updateCarInoutCarNum(CarInoutDto carInoutDto) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 修改在场车辆车牌号
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResultVo getManualOpenDoorLogs(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }


    /**
     * 修改在场车辆车牌号
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResultVo tempCarAuth(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 查询待审核车辆
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResultVo getTempCarAuths(JSONObject reqJson) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }



    /**
     * 查询 临时车待支付订单
     *
     * @param tempCarPayOrderDto
     * @return
     */
    @Override
    public ResultVo notifyTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }


    /**
     * 业主处理执行
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) throws Exception {

    }



    /**
     * 查询模板信息
     * @param communityId
     * @param specCd
     * @return
     */
    protected TemplateDataDto getOwnerTemplateId(String communityId, String specCd){
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return null;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityId);
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(specCd);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return null;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());

        return new TemplateDataDto(templateId,accessToken,smallWeChatDtos.get(0).getAppId());
    }
}
