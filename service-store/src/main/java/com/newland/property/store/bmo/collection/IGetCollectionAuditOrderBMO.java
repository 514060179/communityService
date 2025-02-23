package com.newland.property.store.bmo.collection;

import com.newland.property.dto.audit.AuditUser;
import org.springframework.http.ResponseEntity;

public interface IGetCollectionAuditOrderBMO {

    /**
     * 查询待审核单
     * @param auditUser
     * @return
     */
    ResponseEntity<String> auditOrder(AuditUser auditUser);
}
