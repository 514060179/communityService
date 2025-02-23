package com.newland.property.api.components.fee;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.IFeeServiceSMO;
import com.newland.property.api.smo.fee.IListFeeConfigsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName ViewPropertyFeeConfigComponent
 * @Description 展示物业费信息
 * @Author wuxw
 * @Date 2019/6/1 14:33
 * @Version 1.0
 * add by wuxw 2019/6/1
 **/
@Component("payFeeOrder")
public class PayFeeOrderComponent {

    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    @Autowired
    private IListFeeConfigsSMO listFeeConfigsSMOImpl;

    public ResponseEntity<String> loadPropertyConfigData(IPageData pd) {
        return listFeeConfigsSMOImpl.listFeeConfigs(pd);
    }


    /**
     * 缴费
     *
     * @param pd 页面数据封装
     * @return 缴费接口
     */
    public ResponseEntity<String> payFee(IPageData pd) {
        return feeServiceSMOImpl.payFee(pd);
    }


    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
