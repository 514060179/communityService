package com.newland.property.community.smo.impl;

import com.newland.property.community.dao.IDictDao;
import com.newland.property.intf.community.DictInnerServiceSMO;
import com.newland.property.dto.dict.DictDto;
import com.newland.property.dto.dict.DictQueryDto;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
@RestController
public class DictInnerServiceSMOImpl implements DictInnerServiceSMO {

    @Autowired
    private IDictDao iDictDao;

    @Override
    public List<DictDto> queryDict(@RequestBody DictQueryDto queryDto) {
        return this.iDictDao.queryDict(BeanConvertUtil.beanCovertMap(queryDto));
    }
}
