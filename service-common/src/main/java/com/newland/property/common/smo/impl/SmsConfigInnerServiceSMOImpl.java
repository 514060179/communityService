package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.ISmsConfigServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.sms.SmsConfigDto;
import com.newland.property.intf.common.ISmsConfigInnerServiceSMO;
import com.newland.property.po.sms.SmsConfigPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 短信配置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class SmsConfigInnerServiceSMOImpl extends BaseServiceSMO implements ISmsConfigInnerServiceSMO {

    @Autowired
    private ISmsConfigServiceDao smsConfigServiceDaoImpl;


    @Override
    public int saveSmsConfig(@RequestBody SmsConfigPo smsConfigPo) {
        int saveFlag = 1;
        smsConfigServiceDaoImpl.saveSmsConfigInfo(BeanConvertUtil.beanCovertMap(smsConfigPo));
        return saveFlag;
    }

    @Override
    public int updateSmsConfig(@RequestBody SmsConfigPo smsConfigPo) {
        int saveFlag = 1;
        smsConfigServiceDaoImpl.updateSmsConfigInfo(BeanConvertUtil.beanCovertMap(smsConfigPo));
        return saveFlag;
    }

    @Override
    public int deleteSmsConfig(@RequestBody SmsConfigPo smsConfigPo) {
        int saveFlag = 1;
        smsConfigPo.setStatusCd("1");
        smsConfigServiceDaoImpl.updateSmsConfigInfo(BeanConvertUtil.beanCovertMap(smsConfigPo));
        return saveFlag;
    }

    @Override
    public List<SmsConfigDto> querySmsConfigs(@RequestBody SmsConfigDto smsConfigDto) {

        //校验是否传了 分页信息

        int page = smsConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            smsConfigDto.setPage((page - 1) * smsConfigDto.getRow());
        }

        List<SmsConfigDto> smsConfigs = BeanConvertUtil.covertBeanList(smsConfigServiceDaoImpl.getSmsConfigInfo(BeanConvertUtil.beanCovertMap(smsConfigDto)), SmsConfigDto.class);

        return smsConfigs;
    }


    @Override
    public int querySmsConfigsCount(@RequestBody SmsConfigDto smsConfigDto) {
        return smsConfigServiceDaoImpl.querySmsConfigsCount(BeanConvertUtil.beanCovertMap(smsConfigDto));
    }

    public ISmsConfigServiceDao getSmsConfigServiceDaoImpl() {
        return smsConfigServiceDaoImpl;
    }

    public void setSmsConfigServiceDaoImpl(ISmsConfigServiceDao smsConfigServiceDaoImpl) {
        this.smsConfigServiceDaoImpl = smsConfigServiceDaoImpl;
    }
}
