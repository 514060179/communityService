package com.newland.property.fee.bmo.prestoreFee;
import com.newland.property.dto.prestoreFee.PrestoreFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetPrestoreFeeBMO {


    /**
     * 查询预存费用
     * add by wuxw
     * @param  prestoreFeeDto
     * @return
     */
    ResponseEntity<String> get(PrestoreFeeDto prestoreFeeDto);


}
