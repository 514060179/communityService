package com.newland.property.fee.smo.impl;


import com.newland.property.fee.dao.IPrestoreFeeServiceDao;
import com.newland.property.dto.prestoreFee.PrestoreFeeDto;
import com.newland.property.intf.fee.IPrestoreFeeInnerServiceSMO;
import com.newland.property.po.prestoreFee.PrestoreFeePo;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 预存费用内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PrestoreFeeInnerServiceSMOImpl extends BaseServiceSMO implements IPrestoreFeeInnerServiceSMO {

    @Autowired
    private IPrestoreFeeServiceDao prestoreFeeServiceDaoImpl;


    @Override
    public int savePrestoreFee(@RequestBody PrestoreFeePo prestoreFeePo) {
        int saveFlag = 1;
        prestoreFeeServiceDaoImpl.savePrestoreFeeInfo(BeanConvertUtil.beanCovertMap(prestoreFeePo));
        return saveFlag;
    }

     @Override
    public int updatePrestoreFee(@RequestBody  PrestoreFeePo prestoreFeePo) {
        int saveFlag = 1;
         prestoreFeeServiceDaoImpl.updatePrestoreFeeInfo(BeanConvertUtil.beanCovertMap(prestoreFeePo));
        return saveFlag;
    }

     @Override
    public int deletePrestoreFee(@RequestBody  PrestoreFeePo prestoreFeePo) {
        int saveFlag = 1;
        prestoreFeePo.setStatusCd("1");
        prestoreFeeServiceDaoImpl.updatePrestoreFeeInfo(BeanConvertUtil.beanCovertMap(prestoreFeePo));
        return saveFlag;
    }

    @Override
    public List<PrestoreFeeDto> queryPrestoreFees(@RequestBody  PrestoreFeeDto prestoreFeeDto) {

        //校验是否传了 分页信息

        int page = prestoreFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            prestoreFeeDto.setPage((page - 1) * prestoreFeeDto.getRow());
        }

        List<PrestoreFeeDto> prestoreFees = BeanConvertUtil.covertBeanList(prestoreFeeServiceDaoImpl.getPrestoreFeeInfo(BeanConvertUtil.beanCovertMap(prestoreFeeDto)), PrestoreFeeDto.class);

        return prestoreFees;
    }


    @Override
    public int queryPrestoreFeesCount(@RequestBody PrestoreFeeDto prestoreFeeDto) {
        return prestoreFeeServiceDaoImpl.queryPrestoreFeesCount(BeanConvertUtil.beanCovertMap(prestoreFeeDto));    }

    public IPrestoreFeeServiceDao getPrestoreFeeServiceDaoImpl() {
        return prestoreFeeServiceDaoImpl;
    }

    public void setPrestoreFeeServiceDaoImpl(IPrestoreFeeServiceDao prestoreFeeServiceDaoImpl) {
        this.prestoreFeeServiceDaoImpl = prestoreFeeServiceDaoImpl;
    }
}
