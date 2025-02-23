package com.newland.property.store.bmo.storeAds;
import com.newland.property.po.store.StoreAdsPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreAdsBMO {


    /**
     * 修改商户广告
     * add by wuxw
     * @param storeAdsPo
     * @return
     */
    ResponseEntity<String> delete(StoreAdsPo storeAdsPo);


}
