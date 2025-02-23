package com.newland.property.common.bmo.logSystemError;
import com.newland.property.dto.log.LogSystemErrorDto;
import org.springframework.http.ResponseEntity;
public interface IGetLogSystemErrorBMO {


    /**
     * 查询系统异常
     * add by wuxw
     * @param  logSystemErrorDto
     * @return
     */
    ResponseEntity<String> get(LogSystemErrorDto logSystemErrorDto);


}
