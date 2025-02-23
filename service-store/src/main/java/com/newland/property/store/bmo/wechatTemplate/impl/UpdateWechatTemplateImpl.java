package com.newland.property.store.bmo.wechatTemplate.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.newland.property.po.wechat.WechatSmsTemplatePo;
import com.newland.property.store.bmo.wechatTemplate.IUpdateWechatTemplate;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateWechatTemplateImpl")
public class UpdateWechatTemplateImpl implements IUpdateWechatTemplate {
    @Autowired
    private IWechatSmsTemplateInnerServiceSMO wechatSmsTemplateInnerServiceSMOImpl;

    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(WechatSmsTemplatePo wechatSmsTemplatePo) {
        int flag = wechatSmsTemplateInnerServiceSMOImpl.updateWechatSmsTemplate(wechatSmsTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}
