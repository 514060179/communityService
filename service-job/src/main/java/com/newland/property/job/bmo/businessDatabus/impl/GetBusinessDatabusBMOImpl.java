package com.newland.property.job.bmo.businessDatabus.impl;

import com.newland.property.dto.system.BusinessDatabusDto;
import com.newland.property.intf.job.IBusinessDatabusInnerServiceSMO;
import com.newland.property.job.bmo.businessDatabus.IGetBusinessDatabusBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getBusinessDatabusBMOImpl")
public class GetBusinessDatabusBMOImpl implements IGetBusinessDatabusBMO {

    @Autowired
    private IBusinessDatabusInnerServiceSMO businessDatabusInnerServiceSMOImpl;

    /**
     * @param businessDatabusDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(BusinessDatabusDto businessDatabusDto) {


        int count = businessDatabusInnerServiceSMOImpl.queryBusinessDatabussCount(businessDatabusDto);

        List<BusinessDatabusDto> businessDatabusDtos = null;
        if (count > 0) {
            businessDatabusDtos = businessDatabusInnerServiceSMOImpl.queryBusinessDatabuss(businessDatabusDto);
        } else {
            businessDatabusDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) businessDatabusDto.getRow()), count, businessDatabusDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
