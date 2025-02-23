package com.newland.property.acct.bmo.systemGoldSetting.impl;

import com.newland.property.acct.bmo.systemGoldSetting.IDeleteSystemGoldSettingBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.newland.property.po.system.SystemGoldSettingPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSystemGoldSettingBMOImpl")
public class DeleteSystemGoldSettingBMOImpl implements IDeleteSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * @param systemGoldSettingPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(SystemGoldSettingPo systemGoldSettingPo) {

        int flag = systemGoldSettingInnerServiceSMOImpl.deleteSystemGoldSetting(systemGoldSettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
