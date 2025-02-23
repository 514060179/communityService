package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.room.ApplyRoomDiscountTypeDto;
import com.newland.property.fee.dao.IApplyRoomDiscountTypeServiceDao;
import com.newland.property.intf.fee.IApplyRoomDiscountTypeInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountTypePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 优惠申请类型内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplyRoomDiscountTypeInnerServiceSMOImpl extends BaseServiceSMO implements IApplyRoomDiscountTypeInnerServiceSMO {

    @Autowired
    private IApplyRoomDiscountTypeServiceDao applyRoomDiscountTypeServiceDaoImpl;


    @Override
    public int saveApplyRoomDiscountType(@RequestBody ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {
        int saveFlag = 1;
        applyRoomDiscountTypeServiceDaoImpl.saveApplyRoomDiscountTypeInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountTypePo));
        return saveFlag;
    }

    @Override
    public int updateApplyRoomDiscountType(@RequestBody ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {
        int saveFlag = 1;
        applyRoomDiscountTypeServiceDaoImpl.updateApplyRoomDiscountTypeInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountTypePo));
        return saveFlag;
    }

    @Override
    public int deleteApplyRoomDiscountType(@RequestBody ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {
        int saveFlag = 1;
        applyRoomDiscountTypePo.setStatusCd("1");
        applyRoomDiscountTypeServiceDaoImpl.updateApplyRoomDiscountTypeInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountTypePo));
        return saveFlag;
    }

    @Override
    public List<ApplyRoomDiscountTypeDto> queryApplyRoomDiscountTypes(@RequestBody ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto) {

        //校验是否传了 分页信息

        int page = applyRoomDiscountTypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applyRoomDiscountTypeDto.setPage((page - 1) * applyRoomDiscountTypeDto.getRow());
        }

        List<ApplyRoomDiscountTypeDto> applyRoomDiscountTypes = BeanConvertUtil.covertBeanList(applyRoomDiscountTypeServiceDaoImpl.getApplyRoomDiscountTypeInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountTypeDto)), ApplyRoomDiscountTypeDto.class);

        return applyRoomDiscountTypes;
    }


    @Override
    public int queryApplyRoomDiscountTypesCount(@RequestBody ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto) {
        return applyRoomDiscountTypeServiceDaoImpl.queryApplyRoomDiscountTypesCount(BeanConvertUtil.beanCovertMap(applyRoomDiscountTypeDto));
    }

    public IApplyRoomDiscountTypeServiceDao getApplyRoomDiscountTypeServiceDaoImpl() {
        return applyRoomDiscountTypeServiceDaoImpl;
    }

    public void setApplyRoomDiscountTypeServiceDaoImpl(IApplyRoomDiscountTypeServiceDao applyRoomDiscountTypeServiceDaoImpl) {
        this.applyRoomDiscountTypeServiceDaoImpl = applyRoomDiscountTypeServiceDaoImpl;
    }
}
