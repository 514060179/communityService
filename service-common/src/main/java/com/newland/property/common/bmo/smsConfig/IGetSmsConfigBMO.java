package com.newland.property.common.bmo.smsConfig;
import com.newland.property.dto.sms.SmsConfigDto;
import org.springframework.http.ResponseEntity;
public interface IGetSmsConfigBMO {


    /**
     * 查询短信配置
     * add by wuxw
     * @param  smsConfigDto
     * @return
     */
    ResponseEntity<String> get(SmsConfigDto smsConfigDto);


}
