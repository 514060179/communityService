package com.newland.property.job.mall;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.vo.ResultVo;

/**
 *
 */
public interface ISendMall {

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
