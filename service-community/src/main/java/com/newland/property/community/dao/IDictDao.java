package com.newland.property.community.dao;

import com.newland.property.dto.dict.DictDto;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
public interface IDictDao {

    List<DictDto> queryDict(Map param);

}
