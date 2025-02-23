package com.newland.property.fee.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.fee.dao.IFeeConfigServiceDao;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.fee.PayFeeConfigPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用配置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeConfigInnerServiceSMOImpl extends BaseServiceSMO implements IFeeConfigInnerServiceSMO {

    @Autowired
    private IFeeConfigServiceDao feeConfigServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FeeConfigDto> queryFeeConfigs(@RequestBody FeeConfigDto feeConfigDto) {
        //校验是否传了 分页信息
        int page = feeConfigDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            feeConfigDto.setPage((page - 1) * feeConfigDto.getRow());
        }
        List<FeeConfigDto> feeConfigs = BeanConvertUtil.covertBeanList(feeConfigServiceDaoImpl.getFeeConfigInfo(BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeConfigDto.class);
        return feeConfigs;
    }

    @Override
    public int queryFeeConfigsCount(@RequestBody FeeConfigDto feeConfigDto) {
        return feeConfigServiceDaoImpl.queryFeeConfigsCount(BeanConvertUtil.beanCovertMap(feeConfigDto));
    }

    @Override
    public int saveFeeConfig(@RequestBody PayFeeConfigPo payFeeConfigPo) {
        return feeConfigServiceDaoImpl.saveFeeConfig(BeanConvertUtil.beanCovertMap(payFeeConfigPo));
    }

    @Override
    public int deleteFeeConfig(@RequestBody PayFeeConfigPo payFeeConfigPo) {
        return feeConfigServiceDaoImpl.deleteFeeConfig(BeanConvertUtil.beanCovertMap(payFeeConfigPo));
    }

    public IFeeConfigServiceDao getFeeConfigServiceDaoImpl() {
        return feeConfigServiceDaoImpl;
    }

    public void setFeeConfigServiceDaoImpl(IFeeConfigServiceDao feeConfigServiceDaoImpl) {
        this.feeConfigServiceDaoImpl = feeConfigServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
