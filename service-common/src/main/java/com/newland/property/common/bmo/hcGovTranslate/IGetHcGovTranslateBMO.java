package com.newland.property.common.bmo.hcGovTranslate;
import com.newland.property.dto.hcGovTranslate.HcGovTranslateDto;
import org.springframework.http.ResponseEntity;
public interface IGetHcGovTranslateBMO {


    /**
     * 查询社区政务同步
     * add by wuxw
     * @param  hcGovTranslateDto
     * @return
     */
    ResponseEntity<String> get(HcGovTranslateDto hcGovTranslateDto);


}
