package com.newland.property.user.bmo.staffAppAuth;

import com.newland.property.dto.user.StaffAppAuthDto;
import org.springframework.http.ResponseEntity;

public interface IGetStaffAppAuthBMO {


    /**
     * 查询员工微信认证
     * add by wuxw
     *
     * @param staffAppAuthDto
     * @return
     */
    ResponseEntity<String> get(StaffAppAuthDto staffAppAuthDto);


}
