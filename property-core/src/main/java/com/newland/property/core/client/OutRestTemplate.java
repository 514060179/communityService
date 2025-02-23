package com.newland.property.core.client;

import com.newland.property.core.factory.LogFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.ExceptionUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.Date;

/**
 * 该类只要负责调用外部资源
 *
 * @author wux
 * @create 2019-02-02 下午8:28
 * @desc 对RestTemplate类封装
 **/
public class OutRestTemplate extends RestTemplate {

    private static Logger logger = LoggerFactory.getLogger(OutRestTemplate.class);

    // exchange

    /**
     * 重写spring RestTemplate类 加入日志等信息
     *
     * @param url
     * @param method
     * @param requestEntity
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {

        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, method, requestEntity, uriVariables);
            responseEntity = super.exchange(url, method, requestEntity, responseType, uriVariables);
            logger.debug("返回信息：responseEntity:{}", responseEntity);

        } catch (HttpStatusCodeException e) {
            errMsg = ExceptionUtil.getStackTrace(e);
            throw e;
        } finally {

            if (responseEntity != null) {
                tmpResponseEntity = new ResponseEntity(responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                tmpResponseEntity = new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }
            LogFactory.saveOutLog(url, "POST", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null,
                    (requestEntity != null && requestEntity.getBody() != null) ? requestEntity.getBody().toString() : "",
                    tmpResponseEntity);

        }
        return responseEntity;
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request,
                                               Class<T> responseType, Object... uriVariables) throws RestClientException {
        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, "POST", request, uriVariables);
            responseEntity = super.postForEntity(url, request, responseType, uriVariables);
            logger.debug("返回信息：responseEntity:{}", responseEntity);
        } catch (HttpStatusCodeException e) {
            errMsg = ExceptionUtil.getStackTrace(e);
            throw e;
        } finally {

            if (responseEntity != null) {
                tmpResponseEntity = new ResponseEntity(responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                tmpResponseEntity = new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }
            //  saveLog(url, "POST", null, tmpResponseEntity, DateUtil.getCurrentDate().getTime() - startTime.getTime());

            LogFactory.saveOutLog(url, "POST", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null, request.toString(), tmpResponseEntity);
        }
        return responseEntity;
    }

    public <T> ResponseEntity<T> newExchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},request:{}", url, method, requestEntity);
            responseEntity = new RestTemplate().exchange(url, method, requestEntity, responseType);
            logger.debug("返回信息：responseEntity:{}", responseEntity);
        } catch (HttpStatusCodeException e) {
            errMsg = ExceptionUtil.getStackTrace(e);
            throw e;
        } finally {

            if (responseEntity != null) {
                tmpResponseEntity = new ResponseEntity(responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                tmpResponseEntity = new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }
            LogFactory.saveOutLog(url, "POST", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null,
                    (requestEntity != null && requestEntity.getBody() != null) ? requestEntity.getBody().toString() : "",
                    tmpResponseEntity);
        }
        return responseEntity;
    }

}
