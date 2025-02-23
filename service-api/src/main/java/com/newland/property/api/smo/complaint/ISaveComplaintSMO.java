package com.newland.property.api.smo.complaint;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 投诉建议
 */
public interface ISaveComplaintSMO {

    /**
     * 提交投诉建议
     * @param pd
     * @return
     */
    public ResponseEntity<String> save(IPageData pd);

}
