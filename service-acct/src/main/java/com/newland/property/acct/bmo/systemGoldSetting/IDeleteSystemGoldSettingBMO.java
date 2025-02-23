package com.newland.property.acct.bmo.systemGoldSetting;
import com.newland.property.po.system.SystemGoldSettingPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteSystemGoldSettingBMO {


    /**
     * 修改金币设置
     * add by wuxw
     * @param systemGoldSettingPo
     * @return
     */
    ResponseEntity<String> delete(SystemGoldSettingPo systemGoldSettingPo);


}
