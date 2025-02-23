package com.newland.property.job.adapt.hcIotNew.http;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.vo.ResultVo;

/**
 *
 */
public interface ISendIot {

    /***
     * post 请求
     * @param url
     * @param paramIn
     * @return
     */
    ResultVo post(String url, JSONObject paramIn);


    /***
     * post 请求
     * @param url
     * @return
     */
    ResultVo get(String url);
}
