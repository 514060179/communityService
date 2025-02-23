package com.newland.property.user.bmo.staffAppAuth;

import com.newland.property.po.user.StaffAppAuthPo;
import org.springframework.http.ResponseEntity;

public interface ISaveStaffAppAuthBMO {


    /**
     * 添加员工微信认证
     * add by wuxw
     *
     * @param staffAppAuthPo
     * @return
     */
    ResponseEntity<String> save(StaffAppAuthPo staffAppAuthPo);


}
