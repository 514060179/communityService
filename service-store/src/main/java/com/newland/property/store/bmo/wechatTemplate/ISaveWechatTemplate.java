package com.newland.property.store.bmo.wechatTemplate;

import com.newland.property.po.wechat.WechatSmsTemplatePo;
import org.springframework.http.ResponseEntity;

public interface ISaveWechatTemplate {

    /**
     * 保存消息模板
     * @param wechatSmsTemplatePo
     * @return
     */
    ResponseEntity<String> save(WechatSmsTemplatePo wechatSmsTemplatePo);
}
