package com.newland.property.acct.bmo.systemGoldSetting.impl;

import com.newland.property.acct.bmo.systemGoldSetting.IGetSystemGoldSettingBMO;
import com.newland.property.dto.system.SystemGoldSettingDto;
import com.newland.property.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getSystemGoldSettingBMOImpl")
public class GetSystemGoldSettingBMOImpl implements IGetSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * @param systemGoldSettingDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(SystemGoldSettingDto systemGoldSettingDto) {


        int count = systemGoldSettingInnerServiceSMOImpl.querySystemGoldSettingsCount(systemGoldSettingDto);

        List<SystemGoldSettingDto> systemGoldSettingDtos = null;
        if (count > 0) {
            systemGoldSettingDtos = systemGoldSettingInnerServiceSMOImpl.querySystemGoldSettings(systemGoldSettingDto);
        } else {
            systemGoldSettingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) systemGoldSettingDto.getRow()), count, systemGoldSettingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
