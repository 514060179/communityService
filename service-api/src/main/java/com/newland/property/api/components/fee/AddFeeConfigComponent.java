package com.newland.property.api.components.fee;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.fee.IAddFeeConfigSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加费用项组件
 */
@Component("addFeeConfig")
public class AddFeeConfigComponent {

    @Autowired
    private IAddFeeConfigSMO addFeeConfigSMOImpl;

    /**
     * 添加费用项数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addFeeConfigSMOImpl.saveFeeConfig(pd);
    }

    public IAddFeeConfigSMO getAddFeeConfigSMOImpl() {
        return addFeeConfigSMOImpl;
    }

    public void setAddFeeConfigSMOImpl(IAddFeeConfigSMO addFeeConfigSMOImpl) {
        this.addFeeConfigSMOImpl = addFeeConfigSMOImpl;
    }
}
