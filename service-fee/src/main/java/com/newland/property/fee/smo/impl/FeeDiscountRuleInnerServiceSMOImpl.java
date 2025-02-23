package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeDiscountRuleDto;
import com.newland.property.fee.dao.IFeeDiscountRuleServiceDao;
import com.newland.property.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountRulePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用折扣规则内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDiscountRuleInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDiscountRuleInnerServiceSMO {

    @Autowired
    private IFeeDiscountRuleServiceDao feeDiscountRuleServiceDaoImpl;


    @Override
    public int saveFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo) {
        int saveFlag = 1;
        feeDiscountRuleServiceDaoImpl.saveFeeDiscountRuleInfo(BeanConvertUtil.beanCovertMap(feeDiscountRulePo));
        return saveFlag;
    }

    @Override
    public int updateFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo) {
        int saveFlag = 1;
        feeDiscountRuleServiceDaoImpl.updateFeeDiscountRuleInfo(BeanConvertUtil.beanCovertMap(feeDiscountRulePo));
        return saveFlag;
    }

    @Override
    public int deleteFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo) {
        int saveFlag = 1;
        feeDiscountRulePo.setStatusCd("1");
        feeDiscountRuleServiceDaoImpl.updateFeeDiscountRuleInfo(BeanConvertUtil.beanCovertMap(feeDiscountRulePo));
        return saveFlag;
    }

    @Override
    public List<FeeDiscountRuleDto> queryFeeDiscountRules(@RequestBody FeeDiscountRuleDto feeDiscountRuleDto) {

        //校验是否传了 分页信息

        int page = feeDiscountRuleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDiscountRuleDto.setPage((page - 1) * feeDiscountRuleDto.getRow());
        }

        List<FeeDiscountRuleDto> feeDiscountRules = BeanConvertUtil.covertBeanList(feeDiscountRuleServiceDaoImpl.getFeeDiscountRuleInfo(BeanConvertUtil.beanCovertMap(feeDiscountRuleDto)), FeeDiscountRuleDto.class);

        return feeDiscountRules;
    }


    @Override
    public int queryFeeDiscountRulesCount(@RequestBody FeeDiscountRuleDto feeDiscountRuleDto) {
        return feeDiscountRuleServiceDaoImpl.queryFeeDiscountRulesCount(BeanConvertUtil.beanCovertMap(feeDiscountRuleDto));
    }

    public IFeeDiscountRuleServiceDao getFeeDiscountRuleServiceDaoImpl() {
        return feeDiscountRuleServiceDaoImpl;
    }

    public void setFeeDiscountRuleServiceDaoImpl(IFeeDiscountRuleServiceDao feeDiscountRuleServiceDaoImpl) {
        this.feeDiscountRuleServiceDaoImpl = feeDiscountRuleServiceDaoImpl;
    }
}
