package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.renting.RentingPoolDto;
import com.newland.property.intf.user.IRentingPoolInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolPo;
import com.newland.property.user.dao.IRentingPoolServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 房屋出租内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RentingPoolInnerServiceSMOImpl extends BaseServiceSMO implements IRentingPoolInnerServiceSMO {

    @Autowired
    private IRentingPoolServiceDao rentingPoolServiceDaoImpl;


    @Override
    public int saveRentingPool(@RequestBody RentingPoolPo rentingPoolPo) {
        int saveFlag = 1;
        rentingPoolServiceDaoImpl.saveRentingPoolInfo(BeanConvertUtil.beanCovertMap(rentingPoolPo));
        return saveFlag;
    }

    @Override
    public int updateRentingPool(@RequestBody RentingPoolPo rentingPoolPo) {
        int saveFlag = 1;
        rentingPoolServiceDaoImpl.updateRentingPoolInfo(BeanConvertUtil.beanCovertMap(rentingPoolPo));
        return saveFlag;
    }

    @Override
    public int deleteRentingPool(@RequestBody RentingPoolPo rentingPoolPo) {
        int saveFlag = 1;
        rentingPoolPo.setStatusCd("1");
        rentingPoolServiceDaoImpl.updateRentingPoolInfo(BeanConvertUtil.beanCovertMap(rentingPoolPo));
        return saveFlag;
    }

    @Override
    public List<RentingPoolDto> queryRentingPools(@RequestBody RentingPoolDto rentingPoolDto) {

        //校验是否传了 分页信息

        int page = rentingPoolDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            rentingPoolDto.setPage((page - 1) * rentingPoolDto.getRow());
        }

        List<RentingPoolDto> rentingPools = BeanConvertUtil.covertBeanList(rentingPoolServiceDaoImpl.getRentingPoolInfo(BeanConvertUtil.beanCovertMap(rentingPoolDto)), RentingPoolDto.class);

        return rentingPools;
    }


    @Override
    public int queryRentingPoolsCount(@RequestBody RentingPoolDto rentingPoolDto) {
        return rentingPoolServiceDaoImpl.queryRentingPoolsCount(BeanConvertUtil.beanCovertMap(rentingPoolDto));
    }

    public IRentingPoolServiceDao getRentingPoolServiceDaoImpl() {
        return rentingPoolServiceDaoImpl;
    }

    public void setRentingPoolServiceDaoImpl(IRentingPoolServiceDao rentingPoolServiceDaoImpl) {
        this.rentingPoolServiceDaoImpl = rentingPoolServiceDaoImpl;
    }
}
