/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.boot.controller;

import com.newland.property.boot.smo.privilege.IPrivilegeSMO;
import com.newland.property.core.base.controller.BaseController;
import com.newland.property.core.context.IPageData;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.util.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 流程控制类
 */
@Controller
public class FlowController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FlowController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPrivilegeSMO privilegeSMOImpl;

    /**
     * 流程处理方法
     *
     * @param flowCode 流程编码
     * @param request  请求对象
     * @return 页面名称
     */
    @RequestMapping(path = "/flow/{flowCode}")
    public String flow(@PathVariable String flowCode,
                       HttpServletRequest request) {
        logger.debug("请求流程 {},{}", flowCode, new Date());
        try {
            System.out.println("开始寻找组件数据");
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            System.out.println("数据获取成功");
            //权限校验
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/" + flowCode);
            validateFlowData(flowCode, pd);

        } catch (Throwable e) {
            flowCode = "error";
        }
        System.out.println("传入的路径为  " + flowCode);

        return flowCode;
    }


    /**
     * 流程数据校验方法
     *
     * @throws RuntimeException
     */
    private void validateFlowData(String flowCode, IPageData pd) throws RuntimeException {

        Assert.hasLength(flowCode, "参数错误，未传入流程编码");
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
