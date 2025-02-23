package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IAttrValueServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.attrSpec.AttrValueDto;
import com.newland.property.intf.common.IAttrValueInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrValuePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 属性值内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttrValueInnerServiceSMOImpl extends BaseServiceSMO implements IAttrValueInnerServiceSMO {

    @Autowired
    private IAttrValueServiceDao attrValueServiceDaoImpl;


    @Override
    public int saveAttrValue(@RequestBody AttrValuePo attrValuePo) {
        int saveFlag = 1;
        attrValueServiceDaoImpl.saveAttrValueInfo(BeanConvertUtil.beanCovertMap(attrValuePo));
        return saveFlag;
    }

    @Override
    public int updateAttrValue(@RequestBody AttrValuePo attrValuePo) {
        int saveFlag = 1;
        attrValueServiceDaoImpl.updateAttrValueInfo(BeanConvertUtil.beanCovertMap(attrValuePo));
        return saveFlag;
    }

    @Override
    public int deleteAttrValue(@RequestBody AttrValuePo attrValuePo) {
        int saveFlag = 1;
        attrValuePo.setStatusCd("1");
        attrValueServiceDaoImpl.updateAttrValueInfo(BeanConvertUtil.beanCovertMap(attrValuePo));
        return saveFlag;
    }

    @Override
    public List<AttrValueDto> queryAttrValues(@RequestBody AttrValueDto attrValueDto) {

        //校验是否传了 分页信息

        int page = attrValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attrValueDto.setPage((page - 1) * attrValueDto.getRow());
        }

        List<AttrValueDto> attrValues = BeanConvertUtil.covertBeanList(attrValueServiceDaoImpl.getAttrValueInfo(BeanConvertUtil.beanCovertMap(attrValueDto)), AttrValueDto.class);

        return attrValues;
    }


    @Override
    public int queryAttrValuesCount(@RequestBody AttrValueDto attrValueDto) {
        return attrValueServiceDaoImpl.queryAttrValuesCount(BeanConvertUtil.beanCovertMap(attrValueDto));
    }

    public IAttrValueServiceDao getAttrValueServiceDaoImpl() {
        return attrValueServiceDaoImpl;
    }

    public void setAttrValueServiceDaoImpl(IAttrValueServiceDao attrValueServiceDaoImpl) {
        this.attrValueServiceDaoImpl = attrValueServiceDaoImpl;
    }
}
