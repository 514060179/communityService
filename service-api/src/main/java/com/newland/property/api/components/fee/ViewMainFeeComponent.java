package com.newland.property.api.components.fee;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.IFeeServiceSMO;
import com.newland.property.api.smo.fee.IListFeeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName ViewPropertyFeeConfigComponent
 * @Description 查询主费用信息
 * @Author wuxw
 * @Date 2019/6/1 14:33
 * @Version 1.0
 * add by wuxw 2019/6/1
 **/
@Component("viewMainFee")
public class ViewMainFeeComponent {

    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    @Autowired
    private IListFeeSMO listFeeSMOImpl;

    public ResponseEntity<String> getFee(IPageData pd) {
        return listFeeSMOImpl.list(pd);
    }


    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
