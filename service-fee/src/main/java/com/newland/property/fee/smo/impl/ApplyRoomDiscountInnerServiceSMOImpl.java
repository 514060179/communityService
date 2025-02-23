package com.newland.property.fee.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.room.ApplyRoomDiscountDto;
import com.newland.property.fee.dao.IApplyRoomDiscountServiceDao;
import com.newland.property.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 房屋折扣申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplyRoomDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IApplyRoomDiscountInnerServiceSMO {

    @Autowired
    private IApplyRoomDiscountServiceDao applyRoomDiscountServiceDaoImpl;


    @Override
    public int saveApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountServiceDaoImpl.saveApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        return saveFlag;
    }

    @Override
    public int updateApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountServiceDaoImpl.updateApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        return saveFlag;
    }

    @Override
    public int deleteApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountPo.setStatusCd("1");
        applyRoomDiscountServiceDaoImpl.updateApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        return saveFlag;
    }

    @Override
    public List<ApplyRoomDiscountDto> queryApplyRoomDiscounts(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto) {

        //校验是否传了 分页信息

        int page = applyRoomDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applyRoomDiscountDto.setPage((page - 1) * applyRoomDiscountDto.getRow());
        }

        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto)), ApplyRoomDiscountDto.class);

        return applyRoomDiscounts;
    }

    @Override
    public List<ApplyRoomDiscountDto> queryFirstApplyRoomDiscounts(ApplyRoomDiscountDto applyRoomDiscountDto) {
        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(applyRoomDiscountServiceDaoImpl.queryFirstApplyRoomDiscounts(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto)), ApplyRoomDiscountDto.class);
        return applyRoomDiscounts;
    }


    @Override
    public int queryApplyRoomDiscountsCount(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto) {
        return applyRoomDiscountServiceDaoImpl.queryApplyRoomDiscountsCount(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto));
    }

    public IApplyRoomDiscountServiceDao getApplyRoomDiscountServiceDaoImpl() {
        return applyRoomDiscountServiceDaoImpl;
    }

    public void setApplyRoomDiscountServiceDaoImpl(IApplyRoomDiscountServiceDao applyRoomDiscountServiceDaoImpl) {
        this.applyRoomDiscountServiceDaoImpl = applyRoomDiscountServiceDaoImpl;
    }
}
