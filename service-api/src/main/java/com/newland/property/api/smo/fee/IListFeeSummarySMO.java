package com.newland.property.api.smo.fee;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IListFeeSummarySMO {
    public ResponseEntity<String> list(IPageData pd);
}
