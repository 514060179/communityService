package com.newland.property.store.bmo.wechatTemplate;

import com.newland.property.dto.wechat.WechatSmsTemplateDto;
import org.springframework.http.ResponseEntity;

public interface IGetWechatTemplate {

    /**
     * 查询消息模板
     * @param wechatSmsTemplateDto
     * @return
     */
    ResponseEntity<String> get(WechatSmsTemplateDto wechatSmsTemplateDto);
}
