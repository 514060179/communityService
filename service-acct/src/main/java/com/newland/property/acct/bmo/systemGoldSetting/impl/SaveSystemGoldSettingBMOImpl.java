package com.newland.property.acct.bmo.systemGoldSetting.impl;

import com.newland.property.acct.bmo.systemGoldSetting.ISaveSystemGoldSettingBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.newland.property.po.system.SystemGoldSettingPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveSystemGoldSettingBMOImpl")
public class SaveSystemGoldSettingBMOImpl implements ISaveSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param systemGoldSettingPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(SystemGoldSettingPo systemGoldSettingPo) {

        systemGoldSettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));
        int flag = systemGoldSettingInnerServiceSMOImpl.saveSystemGoldSetting(systemGoldSettingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
