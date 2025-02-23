package com.newland.property.fee.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.fee.bmo.ApiBaseBMO;
import com.newland.property.fee.bmo.account.ISaveAccountBMO;
import com.newland.property.po.account.AccountPo;
import com.newland.property.po.account.AccountDetailPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

@Service("saveAccountBMOImpl")
public class SaveAccountBMOImpl extends ApiBaseBMO implements ISaveAccountBMO {

    /**
     * 添加账户余额
     *
     * @author fqz
     * @date 2021-09-09
     */
    @Override
    public void save(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.insert(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT);
    }

    /**
     * 添加账户明细
     *
     * @author fqz
     * @date 2021-09-09
     */
    @Override
    public void saveDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountDetailPo accountDetailPo = BeanConvertUtil.covertBean(paramInJson, AccountDetailPo.class);
        super.insert(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT_DETAIL);
    }

}
