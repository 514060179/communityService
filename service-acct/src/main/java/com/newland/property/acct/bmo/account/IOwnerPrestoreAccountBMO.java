package com.newland.property.acct.bmo.account;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.po.account.AccountDetailPo;
import org.springframework.http.ResponseEntity;

public interface IOwnerPrestoreAccountBMO {


    /**
     * 业主预存
     * add by wuxw
     * @param  accountDetailPo
     * @return
     */
    ResponseEntity<String> prestore(AccountDetailPo accountDetailPo, JSONObject reqJson);


}
