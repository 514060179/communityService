package com.newland.property.fee.bmo.prestoreFee;
import com.newland.property.po.prestoreFee.PrestoreFeePo;
import org.springframework.http.ResponseEntity;

public interface IDeletePrestoreFeeBMO {


    /**
     * 修改预存费用
     * add by wuxw
     * @param prestoreFeePo
     * @return
     */
    ResponseEntity<String> delete(PrestoreFeePo prestoreFeePo);


}
