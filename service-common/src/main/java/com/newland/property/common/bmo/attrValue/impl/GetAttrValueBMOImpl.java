package com.newland.property.common.bmo.attrValue.impl;

import com.newland.property.common.bmo.attrValue.IGetAttrValueBMO;
import com.newland.property.dto.attrSpec.AttrValueDto;
import com.newland.property.intf.common.IAttrValueInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttrValueBMOImpl")
public class GetAttrValueBMOImpl implements IGetAttrValueBMO {

    @Autowired
    private IAttrValueInnerServiceSMO attrValueInnerServiceSMOImpl;

    /**
     * @param attrValueDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AttrValueDto attrValueDto) {


        int count = attrValueInnerServiceSMOImpl.queryAttrValuesCount(attrValueDto);

        List<AttrValueDto> attrValueDtos = null;
        if (count > 0) {
            attrValueDtos = attrValueInnerServiceSMOImpl.queryAttrValues(attrValueDto);
        } else {
            attrValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attrValueDto.getRow()), count, attrValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
