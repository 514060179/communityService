package com.newland.property.community.bmo.communitySetting.impl;


import com.newland.property.community.bmo.communitySetting.IGetCommunitySettingBMO;
import com.newland.property.intf.community.ICommunitySettingInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.newland.property.dto.community.CommunitySettingDto;

import java.util.ArrayList;
import java.util.List;

@Service("getCommunitySettingBMOImpl")
public class GetCommunitySettingBMOImpl implements IGetCommunitySettingBMO {

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    /**
     *
     *
     * @param  communitySettingDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(CommunitySettingDto communitySettingDto) {


        int count = communitySettingInnerServiceSMOImpl.queryCommunitySettingsCount(communitySettingDto);

        List<CommunitySettingDto> communitySettingDtos = null;
        if (count > 0) {
            communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
        } else {
            communitySettingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) communitySettingDto.getRow()), count, communitySettingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
