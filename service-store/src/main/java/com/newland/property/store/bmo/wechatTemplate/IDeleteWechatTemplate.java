package com.newland.property.store.bmo.wechatTemplate;

import com.newland.property.po.wechat.WechatSmsTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteWechatTemplate {

    /**
     * 保存消息模板
     * @param wechatSmsTemplatePo
     * @return
     */
    ResponseEntity<String> delete(WechatSmsTemplatePo wechatSmsTemplatePo);
}
