package com.newland.property.common.bmo.smsConfig.impl;

import com.newland.property.common.bmo.smsConfig.ISaveSmsConfigBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.sms.SmsConfigDto;
import com.newland.property.intf.common.ISmsConfigInnerServiceSMO;
import com.newland.property.po.sms.SmsConfigPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveSmsConfigBMOImpl")
public class SaveSmsConfigBMOImpl implements ISaveSmsConfigBMO {

    @Autowired
    private ISmsConfigInnerServiceSMO smsConfigInnerServiceSMOImpl;


    /**
     * 添加小区信息
     *
     * @param smsConfigPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(SmsConfigPo smsConfigPo) {

        //判断是否重复配置
        SmsConfigDto smsConfigDto = new SmsConfigDto();
        smsConfigDto.setObjId(smsConfigPo.getObjId());
        smsConfigDto.setSmsBusi(smsConfigPo.getSmsBusi());
        List<SmsConfigDto> smsConfigDtos = smsConfigInnerServiceSMOImpl.querySmsConfigs(smsConfigDto);
        if (smsConfigDtos != null && smsConfigDtos.size() > 0) {
            throw new IllegalArgumentException("已经配置 请勿重复配置");
        }
        smsConfigPo.setSmsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_smsId));
        int flag = smsConfigInnerServiceSMOImpl.saveSmsConfig(smsConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
