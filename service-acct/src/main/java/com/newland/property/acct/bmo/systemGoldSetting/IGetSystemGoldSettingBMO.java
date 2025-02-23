package com.newland.property.acct.bmo.systemGoldSetting;
import com.newland.property.dto.system.SystemGoldSettingDto;
import org.springframework.http.ResponseEntity;
public interface IGetSystemGoldSettingBMO {


    /**
     * 查询金币设置
     * add by wuxw
     * @param  systemGoldSettingDto
     * @return
     */
    ResponseEntity<String> get(SystemGoldSettingDto systemGoldSettingDto);


}
