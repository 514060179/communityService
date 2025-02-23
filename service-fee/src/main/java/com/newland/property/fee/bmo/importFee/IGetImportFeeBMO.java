package com.newland.property.fee.bmo.importFee;
import com.newland.property.dto.importData.ImportFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetImportFeeBMO {


    /**
     * 查询费用导入
     * add by wuxw
     * @param  importFeeDto
     * @return
     */
    ResponseEntity<String> get(ImportFeeDto importFeeDto);


}
