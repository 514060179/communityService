package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.renting.RentingConfigDto;
import com.newland.property.intf.user.IRentingConfigInnerServiceSMO;
import com.newland.property.po.renting.RentingConfigPo;
import com.newland.property.user.dao.IRentingConfigServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 房屋出租配置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RentingConfigInnerServiceSMOImpl extends BaseServiceSMO implements IRentingConfigInnerServiceSMO {

    @Autowired
    private IRentingConfigServiceDao rentingConfigServiceDaoImpl;


    @Override
    public int saveRentingConfig(@RequestBody RentingConfigPo rentingConfigPo) {
        int saveFlag = 1;
        rentingConfigServiceDaoImpl.saveRentingConfigInfo(BeanConvertUtil.beanCovertMap(rentingConfigPo));
        return saveFlag;
    }

    @Override
    public int updateRentingConfig(@RequestBody RentingConfigPo rentingConfigPo) {
        int saveFlag = 1;
        rentingConfigServiceDaoImpl.updateRentingConfigInfo(BeanConvertUtil.beanCovertMap(rentingConfigPo));
        return saveFlag;
    }

    @Override
    public int deleteRentingConfig(@RequestBody RentingConfigPo rentingConfigPo) {
        int saveFlag = 1;
        rentingConfigPo.setStatusCd("1");
        rentingConfigServiceDaoImpl.updateRentingConfigInfo(BeanConvertUtil.beanCovertMap(rentingConfigPo));
        return saveFlag;
    }

    @Override
    public List<RentingConfigDto> queryRentingConfigs(@RequestBody RentingConfigDto rentingConfigDto) {

        //校验是否传了 分页信息

        int page = rentingConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            rentingConfigDto.setPage((page - 1) * rentingConfigDto.getRow());
        }

        List<RentingConfigDto> rentingConfigs = BeanConvertUtil.covertBeanList(rentingConfigServiceDaoImpl.getRentingConfigInfo(BeanConvertUtil.beanCovertMap(rentingConfigDto)), RentingConfigDto.class);

        return rentingConfigs;
    }


    @Override
    public int queryRentingConfigsCount(@RequestBody RentingConfigDto rentingConfigDto) {
        return rentingConfigServiceDaoImpl.queryRentingConfigsCount(BeanConvertUtil.beanCovertMap(rentingConfigDto));
    }

    public IRentingConfigServiceDao getRentingConfigServiceDaoImpl() {
        return rentingConfigServiceDaoImpl;
    }

    public void setRentingConfigServiceDaoImpl(IRentingConfigServiceDao rentingConfigServiceDaoImpl) {
        this.rentingConfigServiceDaoImpl = rentingConfigServiceDaoImpl;
    }
}
