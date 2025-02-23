package com.newland.property.common.bmo.hcGovTranslateDetail;
import com.newland.property.po.hcGovTranslate.HcGovTranslateDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteHcGovTranslateDetailBMO {


    /**
     * 修改信息分类
     * add by wuxw
     * @param hcGovTranslateDetailPo
     * @return
     */
    ResponseEntity<String> delete(HcGovTranslateDetailPo hcGovTranslateDetailPo);


}
