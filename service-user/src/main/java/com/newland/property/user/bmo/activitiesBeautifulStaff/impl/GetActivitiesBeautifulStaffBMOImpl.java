package com.newland.property.user.bmo.activitiesBeautifulStaff.impl;

import com.newland.property.dto.activities.ActivitiesBeautifulStaffDto;
import com.newland.property.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.newland.property.user.bmo.activitiesBeautifulStaff.IGetActivitiesBeautifulStaffBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getActivitiesBeautifulStaffBMOImpl")
public class GetActivitiesBeautifulStaffBMOImpl implements IGetActivitiesBeautifulStaffBMO {

    @Autowired
    private IActivitiesBeautifulStaffInnerServiceSMO activitiesBeautifulStaffInnerServiceSMOImpl;

    /**
     * @param activitiesBeautifulStaffDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto) {
        int count = activitiesBeautifulStaffInnerServiceSMOImpl.queryActivitiesBeautifulStaffsCount(activitiesBeautifulStaffDto);
        List<ActivitiesBeautifulStaffDto> activitiesBeautifulStaffDtos = null;
        if (count > 0) {
            activitiesBeautifulStaffDtos = activitiesBeautifulStaffInnerServiceSMOImpl.queryActivitiesBeautifulStaffs(activitiesBeautifulStaffDto);
        } else {
            activitiesBeautifulStaffDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) activitiesBeautifulStaffDto.getRow()), count, activitiesBeautifulStaffDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }
}
