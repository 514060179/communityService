package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IMachineTranslateServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.common.IMachineTranslateInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.machine.MachineTranslateDto;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备同步内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineTranslateInnerServiceSMOImpl extends BaseServiceSMO implements IMachineTranslateInnerServiceSMO {

    @Autowired
    private IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineTranslateDto> queryMachineTranslates(@RequestBody MachineTranslateDto machineTranslateDto) {

        //校验是否传了 分页信息

        int page = machineTranslateDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineTranslateDto.setPage((page - 1) * machineTranslateDto.getRow());
        }

        List<MachineTranslateDto> machineTranslates = BeanConvertUtil.covertBeanList(machineTranslateServiceDaoImpl.getMachineTranslateInfo(BeanConvertUtil.beanCovertMap(machineTranslateDto)), MachineTranslateDto.class);

        return machineTranslates;
    }


    @Override
    public int queryMachineTranslatesCount(@RequestBody MachineTranslateDto machineTranslateDto) {
        return machineTranslateServiceDaoImpl.queryMachineTranslatesCount(BeanConvertUtil.beanCovertMap(machineTranslateDto));
    }

    @Override
    public int updateMachineTranslateState(@RequestBody MachineTranslateDto machineTranslateDto) {
        return machineTranslateServiceDaoImpl.updateMachineTranslateState(BeanConvertUtil.beanCovertMap(machineTranslateDto));
    }

    @Override
    public int saveMachineTranslate(@RequestBody MachineTranslateDto machineTranslateDto) {
        machineTranslateServiceDaoImpl.saveMachineTranslate(BeanConvertUtil.beanCovertMap(machineTranslateDto));
        return 1;
    }

    public IMachineTranslateServiceDao getMachineTranslateServiceDaoImpl() {
        return machineTranslateServiceDaoImpl;
    }

    public void setMachineTranslateServiceDaoImpl(IMachineTranslateServiceDao machineTranslateServiceDaoImpl) {
        this.machineTranslateServiceDaoImpl = machineTranslateServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
