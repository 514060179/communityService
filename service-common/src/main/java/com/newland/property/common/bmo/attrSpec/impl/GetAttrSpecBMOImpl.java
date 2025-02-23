package com.newland.property.common.bmo.attrSpec.impl;

import com.newland.property.common.bmo.attrSpec.IGetAttrSpecBMO;
import com.newland.property.dto.attrSpec.AttrSpecDto;
import com.newland.property.intf.common.IAttrSpecInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttrSpecBMOImpl")
public class GetAttrSpecBMOImpl implements IGetAttrSpecBMO {

    @Autowired
    private IAttrSpecInnerServiceSMO attrSpecInnerServiceSMOImpl;

    /**
     * @param attrSpecDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AttrSpecDto attrSpecDto) {


        int count = attrSpecInnerServiceSMOImpl.queryAttrSpecsCount(attrSpecDto);

        List<AttrSpecDto> attrSpecDtos = null;
        if (count > 0) {
            attrSpecDtos = attrSpecInnerServiceSMOImpl.queryAttrSpecs(attrSpecDto);
        } else {
            attrSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attrSpecDto.getRow()), count, attrSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
