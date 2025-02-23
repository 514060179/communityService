package com.newland.property.api.smo.inspection;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IInspectionPlanStateSMO {

    ResponseEntity<String> updateInspectionPlanState(IPageData pd);
}
