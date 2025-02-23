package com.newland.property.community.smo.impl;

import com.newland.property.community.dao.IPatrolBuildingV1ServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.patrolBuilding.PatrolBuildingDto;
import com.newland.property.intf.community.IPatrolBuildingV1InnerServiceSMO;
import com.newland.property.po.patrolBuilding.PatrolBuildingPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IPatrolBuildingV1InnerServiceSMOImpl extends BaseServiceSMO implements IPatrolBuildingV1InnerServiceSMO {

    @Autowired
    private IPatrolBuildingV1ServiceDao iPatrolBuildingV1ServiceDaoImpl;

    @Override
    public int savePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo) {
        int saveFlag = iPatrolBuildingV1ServiceDaoImpl.savePatrolBuildingInfo(BeanConvertUtil.beanCovertMap(patrolBuildingPo));
        return saveFlag;
    }

    @Override
    public int updatePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo) {
        int saveFlag = iPatrolBuildingV1ServiceDaoImpl.updatePatrolBuildingInfo(BeanConvertUtil.beanCovertMap(patrolBuildingPo));
        return saveFlag;
    }

    @Override
    public int deletePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo) {
        patrolBuildingPo.setStatusCd("1");
        int saveFlag = iPatrolBuildingV1ServiceDaoImpl.updatePatrolBuildingInfo(BeanConvertUtil.beanCovertMap(patrolBuildingPo));
        return saveFlag;
    }

    @Override
    public List<PatrolBuildingDto> queryPatrolBuildings(@RequestBody PatrolBuildingDto patrolBuildingDto) {
        //校验是否传了 分页信息
        int page = patrolBuildingDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            patrolBuildingDto.setPage((page - 1) * patrolBuildingDto.getRow());
        }
        List<PatrolBuildingDto> patrolBuildingDtoList = BeanConvertUtil.covertBeanList(iPatrolBuildingV1ServiceDaoImpl.getPatrolBuildingInfo(BeanConvertUtil.beanCovertMap(patrolBuildingDto)), PatrolBuildingDto.class);
        return patrolBuildingDtoList;
    }

    @Override
    public int queryPatrolBuildingsCount(@RequestBody PatrolBuildingDto patrolBuildingDto) {
        return iPatrolBuildingV1ServiceDaoImpl.queryPatrolBuildingsCount(BeanConvertUtil.beanCovertMap(patrolBuildingDto));
    }
}
