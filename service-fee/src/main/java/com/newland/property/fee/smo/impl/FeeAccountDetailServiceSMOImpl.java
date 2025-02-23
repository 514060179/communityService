package com.newland.property.fee.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeAccountDetailDto;
import com.newland.property.fee.dao.IFeeAccountDetailServiceDao;
import com.newland.property.intf.fee.IFeeAccountDetailServiceSMO;
import com.newland.property.po.fee.FeeAccountDetailPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeeAccountDetailServiceSMOImpl extends BaseServiceSMO implements IFeeAccountDetailServiceSMO {

    @Autowired
    private IFeeAccountDetailServiceDao feeAccountDetailServiceDaoImpl;

    @Override
    public List<FeeAccountDetailDto> queryFeeAccountDetails(@RequestBody FeeAccountDetailDto feeAccountDetailDto) {
        //校验是否传了 分页信息

        int page = feeAccountDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeAccountDetailDto.setPage((page - 1) * feeAccountDetailDto.getRow());
        }

        List<FeeAccountDetailDto> feeAccountDetails = BeanConvertUtil.covertBeanList(feeAccountDetailServiceDaoImpl.getFeeAccountDetailsInfo(BeanConvertUtil.beanCovertMap(feeAccountDetailDto)), FeeAccountDetailDto.class);

        return feeAccountDetails;
    }

    @Override
    public int queryFeeAccountDetailsCount(@RequestBody FeeAccountDetailDto feeAccountDetailDto) {
        return feeAccountDetailServiceDaoImpl.queryFeeAccountDetailsCount(BeanConvertUtil.beanCovertMap(feeAccountDetailDto));
    }

    @Override
    public int saveFeeAccountDetail(@RequestBody FeeAccountDetailPo feeAccountDetailPo) {
        feeAccountDetailServiceDaoImpl.saveFeeAccountDetail(BeanConvertUtil.beanCovertMap(feeAccountDetailPo));
        return 1;
    }

}
