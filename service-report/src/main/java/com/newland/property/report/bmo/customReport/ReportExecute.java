package com.newland.property.report.bmo.customReport;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.db.dao.IQueryServiceDAO;

public interface ReportExecute {

    String execute(JSONObject params,IQueryServiceDAO queryServiceDAOImpl);
}
