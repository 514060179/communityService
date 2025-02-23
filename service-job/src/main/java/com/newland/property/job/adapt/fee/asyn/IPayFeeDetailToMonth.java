package com.newland.property.job.adapt.fee.asyn;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.system.Business;

public interface IPayFeeDetailToMonth {

    void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail);
}
