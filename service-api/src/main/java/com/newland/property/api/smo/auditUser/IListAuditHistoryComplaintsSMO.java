package com.newland.property.api.smo.auditUser;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 查询审核单
 */
public interface IListAuditHistoryComplaintsSMO {

    /**
     * 查询审核订单
     * @param pd
     * @return
     */
    public ResponseEntity<String> listAuditHistoryComplaints(IPageData pd);
}
