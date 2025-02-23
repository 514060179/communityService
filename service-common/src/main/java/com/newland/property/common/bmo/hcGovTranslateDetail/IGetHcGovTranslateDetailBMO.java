package com.newland.property.common.bmo.hcGovTranslateDetail;
import com.newland.property.dto.hcGovTranslate.HcGovTranslateDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetHcGovTranslateDetailBMO {


    /**
     * 查询信息分类
     * add by wuxw
     * @param  hcGovTranslateDetailDto
     * @return
     */
    ResponseEntity<String> get(HcGovTranslateDetailDto hcGovTranslateDetailDto);


}
