package com.newland.property.fee.bmo.feeCollectionDetail;
import com.newland.property.dto.fee.FeeCollectionDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeCollectionDetailBMO {


    /**
     * 查询催缴单
     * add by wuxw
     * @param  feeCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeCollectionDetailDto feeCollectionDetailDto);


}
