package com.newland.property.common.bmo.smsConfig;

import com.newland.property.po.sms.SmsConfigPo;
import org.springframework.http.ResponseEntity;
public interface ISaveSmsConfigBMO {


    /**
     * 添加短信配置
     * add by wuxw
     * @param smsConfigPo
     * @return
     */
    ResponseEntity<String> save(SmsConfigPo smsConfigPo);


}
