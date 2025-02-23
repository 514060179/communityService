package com.newland.property.acct.bmo.systemGoldSetting;

import com.newland.property.po.system.SystemGoldSettingPo;
import org.springframework.http.ResponseEntity;
public interface ISaveSystemGoldSettingBMO {


    /**
     * 添加金币设置
     * add by wuxw
     * @param systemGoldSettingPo
     * @return
     */
    ResponseEntity<String> save(SystemGoldSettingPo systemGoldSettingPo);


}
