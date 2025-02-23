package com.newland.property.store.bmo.storeMsg;
import com.newland.property.dto.store.StoreMsgDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreMsgBMO {


    /**
     * 查询商户消息
     * add by wuxw
     * @param  storeMsgDto
     * @return
     */
    ResponseEntity<String> get(StoreMsgDto storeMsgDto);


}
