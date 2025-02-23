package com.newland.property.acct.smo.impl;


import com.newland.property.acct.dao.IAccountDetailServiceDao;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.account.AccountDetailDto;
import com.newland.property.intf.acct.IAccountDetailInnerServiceSMO;
import com.newland.property.po.account.AccountDetailPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 账户交易内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountDetailInnerServiceSMOImpl extends BaseServiceSMO implements IAccountDetailInnerServiceSMO {

    @Autowired
    private IAccountDetailServiceDao accountDetailServiceDaoImpl;

    @Override
    public List<AccountDetailDto> queryAccountDetails(@RequestBody AccountDetailDto accountDetailDto) {

        //校验是否传了 分页信息

        int page = accountDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountDetailDto.setPage((page - 1) * accountDetailDto.getRow());
        }

        List<AccountDetailDto> accountDetails = BeanConvertUtil.covertBeanList(accountDetailServiceDaoImpl.getAccountDetailInfo(BeanConvertUtil.beanCovertMap(accountDetailDto)), AccountDetailDto.class);


        return accountDetails;
    }


    @Override
    public int queryAccountDetailsCount(@RequestBody AccountDetailDto accountDetailDto) {
        return accountDetailServiceDaoImpl.queryAccountDetailsCount(BeanConvertUtil.beanCovertMap(accountDetailDto));
    }

    @Override
    @PropertyTransactional
    public int saveAccountDetails(@RequestBody AccountDetailPo accountDetailPo) {
        return accountDetailServiceDaoImpl.saveAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }
    @Override
    @PropertyTransactional
    public int updateAccountDetails(@RequestBody AccountDetailPo accountDetailPo) {
        return accountDetailServiceDaoImpl.updateAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }

    public IAccountDetailServiceDao getAccountDetailServiceDaoImpl() {
        return accountDetailServiceDaoImpl;
    }

    public void setAccountDetailServiceDaoImpl(IAccountDetailServiceDao accountDetailServiceDaoImpl) {
        this.accountDetailServiceDaoImpl = accountDetailServiceDaoImpl;
    }

}
