package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeManualCollectionDto;
import com.newland.property.fee.dao.IFeeManualCollectionServiceDao;
import com.newland.property.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 人工托收内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeManualCollectionInnerServiceSMOImpl extends BaseServiceSMO implements IFeeManualCollectionInnerServiceSMO {

    @Autowired
    private IFeeManualCollectionServiceDao feeManualCollectionServiceDaoImpl;


    @Override
    public int saveFeeManualCollection(@RequestBody FeeManualCollectionPo feeManualCollectionPo) {
        int saveFlag = 1;
        feeManualCollectionServiceDaoImpl.saveFeeManualCollectionInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionPo));
        return saveFlag;
    }

    @Override
    public int updateFeeManualCollection(@RequestBody FeeManualCollectionPo feeManualCollectionPo) {
        int saveFlag = 1;
        feeManualCollectionServiceDaoImpl.updateFeeManualCollectionInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeManualCollection(@RequestBody FeeManualCollectionPo feeManualCollectionPo) {
        int saveFlag = 1;
        feeManualCollectionPo.setStatusCd("1");
        feeManualCollectionServiceDaoImpl.updateFeeManualCollectionInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionPo));
        return saveFlag;
    }

    @Override
    public List<FeeManualCollectionDto> queryFeeManualCollections(@RequestBody FeeManualCollectionDto feeManualCollectionDto) {

        //校验是否传了 分页信息

        int page = feeManualCollectionDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeManualCollectionDto.setPage((page - 1) * feeManualCollectionDto.getRow());
        }

        List<FeeManualCollectionDto> feeManualCollections = BeanConvertUtil.covertBeanList(feeManualCollectionServiceDaoImpl.getFeeManualCollectionInfo(BeanConvertUtil.beanCovertMap(feeManualCollectionDto)), FeeManualCollectionDto.class);

        return feeManualCollections;
    }


    @Override
    public int queryFeeManualCollectionsCount(@RequestBody FeeManualCollectionDto feeManualCollectionDto) {
        return feeManualCollectionServiceDaoImpl.queryFeeManualCollectionsCount(BeanConvertUtil.beanCovertMap(feeManualCollectionDto));
    }

    public IFeeManualCollectionServiceDao getFeeManualCollectionServiceDaoImpl() {
        return feeManualCollectionServiceDaoImpl;
    }

    public void setFeeManualCollectionServiceDaoImpl(IFeeManualCollectionServiceDao feeManualCollectionServiceDaoImpl) {
        this.feeManualCollectionServiceDaoImpl = feeManualCollectionServiceDaoImpl;
    }
}
