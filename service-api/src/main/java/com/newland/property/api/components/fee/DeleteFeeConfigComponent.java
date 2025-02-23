package com.newland.property.api.components.fee;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.fee.IDeleteFeeConfigSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加费用项组件
 */
@Component("deleteFeeConfig")
public class DeleteFeeConfigComponent {

@Autowired
private IDeleteFeeConfigSMO deleteFeeConfigSMOImpl;

/**
 * 添加费用项数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteFeeConfigSMOImpl.deleteFeeConfig(pd);
    }

public IDeleteFeeConfigSMO getDeleteFeeConfigSMOImpl() {
        return deleteFeeConfigSMOImpl;
    }

public void setDeleteFeeConfigSMOImpl(IDeleteFeeConfigSMO deleteFeeConfigSMOImpl) {
        this.deleteFeeConfigSMOImpl = deleteFeeConfigSMOImpl;
    }
            }
