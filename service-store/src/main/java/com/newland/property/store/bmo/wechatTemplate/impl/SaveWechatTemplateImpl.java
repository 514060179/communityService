package com.newland.property.store.bmo.wechatTemplate.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.newland.property.po.wechat.WechatSmsTemplatePo;
import com.newland.property.store.bmo.wechatTemplate.ISaveWechatTemplate;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveWechatTemplateImpl")
public class SaveWechatTemplateImpl implements ISaveWechatTemplate {
    @Autowired
    private IWechatSmsTemplateInnerServiceSMO wechatSmsTemplateInnerServiceSMOImpl;

    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(WechatSmsTemplatePo wechatSmsTemplatePo) {
        wechatSmsTemplatePo.setTemplateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_weChatId));
        int flag = wechatSmsTemplateInnerServiceSMOImpl.saveWechatSmsTemplate(wechatSmsTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}
