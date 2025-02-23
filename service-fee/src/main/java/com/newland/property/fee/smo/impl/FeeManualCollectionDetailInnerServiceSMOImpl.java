package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeManualCollectionDetailDto;
import com.newland.property.fee.dao.IFeeManualCollectionDetailServiceDao;
import com.newland.property.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 托收明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeManualCollectionDetailInnerServiceSMOImpl extends BaseServiceSMO implements IFeeManualCollectionDetailInnerServiceSMO {

    @Autowired
    private IFeeManualCollectionDetailServiceDao feeManualCollectionDetailServiceDaoImpl;


    @Override
    public int saveFeeManualCollectionDetail(@RequestBody FeeManualCollectionDetailPo feeManualCollectionDetailPo) {
        int saveFlag = 1;
        feeManualCollectionDetailServiceDaoImpl.saveFeeManualCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int updateFeeManualCollectionDetail(@RequestBody FeeManualCollectionDetailPo feeManualCollectionDetailPo) {
        int saveFlag = 1;
        feeManualCollectionDetailServiceDaoImpl.updateFeeManualCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeManualCollectionDetail(@RequestBody FeeManualCollectionDetailPo feeManualCollectionDetailPo) {
        int saveFlag = 1;
        feeManualCollectionDetailPo.setStatusCd("1");
        feeManualCollectionDetailServiceDaoImpl.updateFeeManualCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public List<FeeManualCollectionDetailDto> queryFeeManualCollectionDetails(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto) {

        //校验是否传了 分页信息

        int page = feeManualCollectionDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeManualCollectionDetailDto.setPage((page - 1) * feeManualCollectionDetailDto.getRow());
        }

        List<FeeManualCollectionDetailDto> feeManualCollectionDetails = BeanConvertUtil.covertBeanList(feeManualCollectionDetailServiceDaoImpl.getFeeManualCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailDto)), FeeManualCollectionDetailDto.class);

        return feeManualCollectionDetails;
    }


    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeManualCollectionDetailDto 数据对象分享
     * @return FeeManualCollectionDetailDto 对象数据
     */
    @Override
    public double queryFeeManualCollectionDetailTotalFee(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto){
        return feeManualCollectionDetailServiceDaoImpl.queryFeeManualCollectionDetailTotalFee(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailDto));
    }

    @Override
    public int queryFeeManualCollectionDetailsCount(@RequestBody FeeManualCollectionDetailDto feeManualCollectionDetailDto) {
        return feeManualCollectionDetailServiceDaoImpl.queryFeeManualCollectionDetailsCount(BeanConvertUtil.beanCovertMap(feeManualCollectionDetailDto));
    }

    public IFeeManualCollectionDetailServiceDao getFeeManualCollectionDetailServiceDaoImpl() {
        return feeManualCollectionDetailServiceDaoImpl;
    }

    public void setFeeManualCollectionDetailServiceDaoImpl(IFeeManualCollectionDetailServiceDao feeManualCollectionDetailServiceDaoImpl) {
        this.feeManualCollectionDetailServiceDaoImpl = feeManualCollectionDetailServiceDaoImpl;
    }
}
