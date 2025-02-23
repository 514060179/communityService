package com.newland.property.common.smo.impl;

import com.newland.property.common.dao.IAreaServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.common.IAreaInnerServiceSMO;
import com.newland.property.dto.area.AreaDto;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class AreaInnerServiceSMOImpl extends BaseServiceSMO implements IAreaInnerServiceSMO {

    @Autowired
    private IAreaServiceDao areaServiceDaoImpl;

    @Override
    public List<AreaDto> getArea(@RequestBody AreaDto areaDto) {

        List<AreaDto> areas = BeanConvertUtil.covertBeanList(areaServiceDaoImpl.getAreas(BeanConvertUtil.beanCovertMap(areaDto)), AreaDto.class);

        return areas;
    }

    @Override
    public List<AreaDto> getProvCityArea(@RequestBody AreaDto areaDto) {

        List<AreaDto> areas = BeanConvertUtil.covertBeanList(areaServiceDaoImpl.getProvCityArea(BeanConvertUtil.beanCovertMap(areaDto)), AreaDto.class);

        return areas;
    }

    @Override
    public List<AreaDto> getWholeArea(@RequestBody AreaDto areaDto) {
        String areacode = "";
        if (areaDto.getAreaCode() != null && areaDto.getAreaCode().length() > 5){
            areacode = areaDto.getAreaCode();
        }else{
            areacode = "110101001";//东华门街道
        }
        areaDto.setAreaCode(areacode.substring(0,2));
        List<AreaDto> areas = BeanConvertUtil.covertBeanList(areaServiceDaoImpl.getWholeArea(BeanConvertUtil.beanCovertMap(areaDto)), AreaDto.class);
        List<AreaDto> newlist = new ArrayList();
        AreaDto nowarea = new AreaDto();
        for (AreaDto area :areas){
            if (areacode.equals(area.getAreaCode())){
                newlist.add(area);
                nowarea = area;
                break;
            }
        }
        getTree(areas,nowarea,newlist);
        Collections.reverse(newlist);
        return newlist;
    }

    private void getTree(List<AreaDto> areas,AreaDto area,List<AreaDto> newlist){
        for (AreaDto a :areas){
            if (area.getParentAreaCode().equals(a.getAreaCode())){
                newlist.add(a);
                area = a;
                areas.remove(a);
                getTree(areas,area,newlist);
                break;
            }
        }
    }


}
