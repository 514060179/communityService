package com.newland.property.community.bmo.activitiesType.impl;

import com.newland.property.community.bmo.activitiesType.IGetActivitiesTypeBMO;
import com.newland.property.dto.activities.ActivitiesTypeDto;
import com.newland.property.intf.community.IActivitiesTypeInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getActivitiesTypeBMOImpl")
public class GetActivitiesTypeBMOImpl implements IGetActivitiesTypeBMO {

    @Autowired
    private IActivitiesTypeInnerServiceSMO activitiesTypeInnerServiceSMOImpl;

    /**
     * @param activitiesTypeDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ActivitiesTypeDto activitiesTypeDto) {


        int count = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypesCount(activitiesTypeDto);

        List<ActivitiesTypeDto> activitiesTypeDtos = null;
        if (count > 0) {
            activitiesTypeDtos = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypes(activitiesTypeDto);
        } else {
            activitiesTypeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) activitiesTypeDto.getRow()), count, activitiesTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
