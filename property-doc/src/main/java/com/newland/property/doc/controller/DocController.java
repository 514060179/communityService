package com.newland.property.doc.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.doc.annotation.*;
import com.newland.property.doc.entity.ApiDocDto;
import com.newland.property.doc.entity.CmdDocDto;
import com.newland.property.doc.entity.RequestMappingsDocDto;
import com.newland.property.doc.registrar.ApiDocCmdPublishing;
import com.newland.property.doc.registrar.ApiDocPublishing;
import com.newland.property.utils.factory.ApplicationContextFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/doc")
public class DocController {


    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public ResponseEntity<String> html(
            HttpServletRequest request) {


        ApiDocDto apiDocDto = ApiDocPublishing.getApiDocDto();

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        JSONObject param = new JSONObject();
        param.put("api", apiDocDto);
        param.put("mappings", mappingsDocDtos);
        return new ResponseEntity<>(param.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/api/page", method = RequestMethod.GET)
    public ResponseEntity<String> pages(
            @RequestParam("name") String name,
            @RequestParam("resource") String resource,
            HttpServletRequest request) {

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        RequestMappingsDocDto newMappingsDocDto = null;
        for (RequestMappingsDocDto mappingsDocDto : mappingsDocDtos) {
            if (mappingsDocDto.getName().equals(name)) {
                newMappingsDocDto = mappingsDocDto;
            }
        }

        if (newMappingsDocDto == null) {
            return new ResponseEntity<>("[]", HttpStatus.OK);
        }

        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        if ("boot".equals(newMappingsDocDto.getStartWay())) {
            restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        }
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        try {
            responseEntity = restTemplate.exchange(newMappingsDocDto.getUrl()+"/doc/api/"+resource, HttpMethod.GET, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/api/{resource}", method = RequestMethod.GET)
    public ResponseEntity<String> resourcePages(
            @PathVariable String resource,
            HttpServletRequest request) {

        List<CmdDocDto> cmdDocDtos = ApiDocCmdPublishing.getCmdDocs(resource);

        return new ResponseEntity<>(JSONArray.toJSONString(cmdDocDtos), HttpStatus.OK);
    }


    @RequestMapping(path = "/api/pageContent", method = RequestMethod.GET)
    public ResponseEntity<String> pageContent(
            @RequestParam("name") String name,
            @RequestParam("serviceCode") String serviceCode,
            @RequestParam("resource") String resource,
            HttpServletRequest request) {

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        RequestMappingsDocDto newMappingsDocDto = null;
        for (RequestMappingsDocDto mappingsDocDto : mappingsDocDtos) {
            if (mappingsDocDto.getName().equals(name)) {
                newMappingsDocDto = mappingsDocDto;
            }
        }

        if (newMappingsDocDto == null) {
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }

        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        if ("boot".equals(newMappingsDocDto.getStartWay())) {
            restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        }
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        try {
            responseEntity = restTemplate.exchange(newMappingsDocDto.getUrl()+"/doc/api/"+resource+"/"+serviceCode, HttpMethod.GET, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/api/{resource}/{serviceCode}", method = RequestMethod.GET)
    public ResponseEntity<String> api(@PathVariable String resource,
                                      @PathVariable String serviceCode,
                                      HttpServletRequest request) {
        CmdDocDto cmdDocDto = ApiDocCmdPublishing.getCmdDocs(resource,serviceCode);

        if(cmdDocDto == null){
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }

        JSONObject param = JSONObject.parseObject(JSONObject.toJSONString(cmdDocDto));


        Class<?> clazz = null;
        try {
            clazz = Class.forName(cmdDocDto.getCmdClass());
        } catch (ClassNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
        }


        doNewlandParamsDoc(clazz,param);


        doNewlandResponseDoc(param, clazz);

        doNewlandExampleDoc(param, clazz);

        return new ResponseEntity<>(param.toJSONString(), HttpStatus.OK);
    }

    private void doNewlandExampleDoc(JSONObject param, Class clazz) {
        NewlandPropertyExampleDoc newlandPropertyExampleDoc = AnnotationUtils.findAnnotation(clazz, NewlandPropertyExampleDoc.class);

        if(newlandPropertyExampleDoc == null){
            return ;
        }

        param.put("reqBody", newlandPropertyExampleDoc.reqBody());
        param.put("resBody", newlandPropertyExampleDoc.resBody());
    }

    private void doNewlandResponseDoc(JSONObject param, Class clazz) {
        NewlandPropertyResponseDoc newlandPropertyResponseDoc = AnnotationUtils.findAnnotation(clazz, NewlandPropertyResponseDoc.class);

        if(newlandPropertyResponseDoc == null){
            return ;
        }

        NewlandPropertyParamDoc[] newlandPropertyParamDocs = newlandPropertyResponseDoc.params();

        JSONArray params = new JSONArray();
        JSONObject p = null;
        for(NewlandPropertyParamDoc newlandPropertyParamDoc : newlandPropertyParamDocs){
            p = new JSONObject();
            p.put("name", newlandPropertyParamDoc.name());
            p.put("defaultValue", newlandPropertyParamDoc.defaultValue());
            p.put("remark", newlandPropertyParamDoc.remark());
            p.put("type", newlandPropertyParamDoc.type());
            p.put("length", newlandPropertyParamDoc.length());
            p.put("parentNodeName", newlandPropertyParamDoc.parentNodeName());
            params.add(p);
        }
        param.put("resParam",params);
    }

    private void doNewlandParamsDoc(Class clazz, JSONObject param) {

        NewlandPropertyParamsDoc newlandPropertyParamsDoc = AnnotationUtils.findAnnotation(clazz, NewlandPropertyParamsDoc.class);

        if(newlandPropertyParamsDoc == null){
            return ;
        }

        NewlandPropertyHeaderDoc[] newlandPropertyHeaderDocs = newlandPropertyParamsDoc.headers();

        JSONArray headers = new JSONArray();
        JSONObject header = null;
        for(NewlandPropertyHeaderDoc newlandPropertyHeaderDoc : newlandPropertyHeaderDocs){
            header = new JSONObject();
            header.put("name", newlandPropertyHeaderDoc.name());
            header.put("defaultValue", newlandPropertyHeaderDoc.defaultValue());
            header.put("description", newlandPropertyHeaderDoc.description());
            headers.add(header);
        }

        param.put("headers",headers);


        NewlandPropertyParamDoc[] newlandPropertyParamDocs = newlandPropertyParamsDoc.params();

        JSONArray params = new JSONArray();
        JSONObject p = null;
        for(NewlandPropertyParamDoc newlandPropertyParamDoc : newlandPropertyParamDocs){
            p = new JSONObject();
            p.put("name", newlandPropertyParamDoc.name());
            p.put("defaultValue", newlandPropertyParamDoc.defaultValue());
            p.put("remark", newlandPropertyParamDoc.remark());
            p.put("type", newlandPropertyParamDoc.type());
            p.put("length", newlandPropertyParamDoc.length());
            p.put("parentNodeName", newlandPropertyParamDoc.parentNodeName());
            params.add(p);
        }
        param.put("reqParam",params);
    }
}
