package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.payFee.PayFeeAuditDto;
import com.newland.property.fee.dao.IPayFeeAuditServiceDao;
import com.newland.property.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeAuditPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 缴费审核内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PayFeeAuditInnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeAuditInnerServiceSMO {

    @Autowired
    private IPayFeeAuditServiceDao payFeeAuditServiceDaoImpl;


    @Override
    public int savePayFeeAudit(@RequestBody PayFeeAuditPo payFeeAuditPo) {
        int saveFlag = 1;
        payFeeAuditServiceDaoImpl.savePayFeeAuditInfo(BeanConvertUtil.beanCovertMap(payFeeAuditPo));
        return saveFlag;
    }

    @Override
    public int updatePayFeeAudit(@RequestBody PayFeeAuditPo payFeeAuditPo) {
        int saveFlag = 1;
        payFeeAuditServiceDaoImpl.updatePayFeeAuditInfo(BeanConvertUtil.beanCovertMap(payFeeAuditPo));
        return saveFlag;
    }

    @Override
    public int deletePayFeeAudit(@RequestBody PayFeeAuditPo payFeeAuditPo) {
        int saveFlag = 1;
        payFeeAuditPo.setStatusCd("1");
        payFeeAuditServiceDaoImpl.updatePayFeeAuditInfo(BeanConvertUtil.beanCovertMap(payFeeAuditPo));
        return saveFlag;
    }

    @Override
    public List<PayFeeAuditDto> queryPayFeeAudits(@RequestBody PayFeeAuditDto payFeeAuditDto) {

        //校验是否传了 分页信息

        int page = payFeeAuditDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeAuditDto.setPage((page - 1) * payFeeAuditDto.getRow());
        }

        List<PayFeeAuditDto> payFeeAudits = BeanConvertUtil.covertBeanList(payFeeAuditServiceDaoImpl.getPayFeeAuditInfo(BeanConvertUtil.beanCovertMap(payFeeAuditDto)), PayFeeAuditDto.class);

        return payFeeAudits;
    }


    @Override
    public int queryPayFeeAuditsCount(@RequestBody PayFeeAuditDto payFeeAuditDto) {
        return payFeeAuditServiceDaoImpl.queryPayFeeAuditsCount(BeanConvertUtil.beanCovertMap(payFeeAuditDto));
    }

    public IPayFeeAuditServiceDao getPayFeeAuditServiceDaoImpl() {
        return payFeeAuditServiceDaoImpl;
    }

    public void setPayFeeAuditServiceDaoImpl(IPayFeeAuditServiceDao payFeeAuditServiceDaoImpl) {
        this.payFeeAuditServiceDaoImpl = payFeeAuditServiceDaoImpl;
    }
}
