package com.newland.property.store.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.contract.ContractChangePlanDetailAttrDto;
import com.newland.property.intf.store.IContractChangePlanDetailAttrInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanDetailAttrPo;
import com.newland.property.store.dao.IContractChangePlanDetailAttrServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同变更属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractChangePlanDetailAttrInnerServiceSMOImpl extends BaseServiceSMO implements IContractChangePlanDetailAttrInnerServiceSMO {

    @Autowired
    private IContractChangePlanDetailAttrServiceDao contractChangePlanDetailAttrServiceDaoImpl;


    @Override
    public int saveContractChangePlanDetailAttr(@RequestBody ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {
        int saveFlag = 1;
        contractChangePlanDetailAttrServiceDaoImpl.saveContractChangePlanDetailAttrInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailAttrPo));
        return saveFlag;
    }

    @Override
    public int updateContractChangePlanDetailAttr(@RequestBody ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {
        int saveFlag = 1;
        contractChangePlanDetailAttrServiceDaoImpl.updateContractChangePlanDetailAttrInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailAttrPo));
        return saveFlag;
    }

    @Override
    public int deleteContractChangePlanDetailAttr(@RequestBody ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {
        int saveFlag = 1;
        contractChangePlanDetailAttrPo.setStatusCd("1");
        contractChangePlanDetailAttrServiceDaoImpl.updateContractChangePlanDetailAttrInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailAttrPo));
        return saveFlag;
    }

    @Override
    public List<ContractChangePlanDetailAttrDto> queryContractChangePlanDetailAttrs(@RequestBody ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto) {

        //校验是否传了 分页信息

        int page = contractChangePlanDetailAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractChangePlanDetailAttrDto.setPage((page - 1) * contractChangePlanDetailAttrDto.getRow());
        }

        List<ContractChangePlanDetailAttrDto> contractChangePlanDetailAttrs = BeanConvertUtil.covertBeanList(contractChangePlanDetailAttrServiceDaoImpl.getContractChangePlanDetailAttrInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailAttrDto)), ContractChangePlanDetailAttrDto.class);

        return contractChangePlanDetailAttrs;
    }


    @Override
    public int queryContractChangePlanDetailAttrsCount(@RequestBody ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto) {
        return contractChangePlanDetailAttrServiceDaoImpl.queryContractChangePlanDetailAttrsCount(BeanConvertUtil.beanCovertMap(contractChangePlanDetailAttrDto));
    }

    public IContractChangePlanDetailAttrServiceDao getContractChangePlanDetailAttrServiceDaoImpl() {
        return contractChangePlanDetailAttrServiceDaoImpl;
    }

    public void setContractChangePlanDetailAttrServiceDaoImpl(IContractChangePlanDetailAttrServiceDao contractChangePlanDetailAttrServiceDaoImpl) {
        this.contractChangePlanDetailAttrServiceDaoImpl = contractChangePlanDetailAttrServiceDaoImpl;
    }
}
