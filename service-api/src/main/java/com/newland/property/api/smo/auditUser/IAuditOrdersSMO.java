package com.newland.property.api.smo.auditUser;

import com.newland.property.core.context.IPageData;
import com.newland.property.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

public interface IAuditOrdersSMO {

    ResponseEntity<String> auditOrder(IPageData pd) throws SMOException;
}
