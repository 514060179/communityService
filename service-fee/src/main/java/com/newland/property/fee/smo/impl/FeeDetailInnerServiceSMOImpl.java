package com.newland.property.fee.smo.impl;


import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.fee.dao.IFeeDetailServiceDao;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDetailInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDetailInnerServiceSMO {

    @Autowired
    private IFeeDetailServiceDao feeDetailServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FeeDetailDto> queryFeeDetails(@RequestBody FeeDetailDto feeDetailDto) {

        //校验是否传了 分页信息

        int page = feeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDetailDto.setPage((page - 1) * feeDetailDto.getRow());
        }

        List<FeeDetailDto> feeDetails = BeanConvertUtil.covertBeanList(feeDetailServiceDaoImpl.getFeeDetailInfo(BeanConvertUtil.beanCovertMap(feeDetailDto)), FeeDetailDto.class);

        refreshFeeDetail(feeDetails);
        return feeDetails;
    }

    @Override
    public List<FeeDetailDto> queryBusinessFeeDetails(@RequestBody FeeDetailDto feeDetailDto) {

        //校验是否传了 分页信息

        int page = feeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDetailDto.setPage((page - 1) * feeDetailDto.getRow());
        }

        List<FeeDetailDto> feeDetails = BeanConvertUtil.covertBeanList(feeDetailServiceDaoImpl.getBusinessFeeDetailInfo(BeanConvertUtil.beanCovertMap(feeDetailDto)), FeeDetailDto.class);

        refreshFeeDetail(feeDetails);
        return feeDetails;
    }

    private void refreshFeeDetail(List<FeeDetailDto> feeDetails) {
        if(feeDetails == null || feeDetails.size() < 1){
            return ;
        }

        for(FeeDetailDto feeDetailDto : feeDetails){
            if(!StringUtil.isEmpty(feeDetailDto.getImportFeeName())){
                feeDetailDto.setFeeName(feeDetailDto.getImportFeeName());
            }
        }
    }


    @Override
    public int queryFeeDetailsCount(@RequestBody FeeDetailDto feeDetailDto) {
        return feeDetailServiceDaoImpl.queryFeeDetailsCount(BeanConvertUtil.beanCovertMap(feeDetailDto));
    }

    @Override
    @PropertyTransactional
    public int saveFeeDetail(@RequestBody PayFeeDetailPo payFeeDetailPo) {
        feeDetailServiceDaoImpl.saveFeeDetail(BeanConvertUtil.beanCovertMap(payFeeDetailPo));
        return 1;
    }

    public IFeeDetailServiceDao getFeeDetailServiceDaoImpl() {
        return feeDetailServiceDaoImpl;
    }

    public void setFeeDetailServiceDaoImpl(IFeeDetailServiceDao feeDetailServiceDaoImpl) {
        this.feeDetailServiceDaoImpl = feeDetailServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
