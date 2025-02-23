package com.newland.property.core.trace;

import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.trace.TraceAnnotationsDto;
import com.newland.property.dto.trace.TraceDto;
import com.newland.property.utils.constant.CommonConstant;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * restremplate 请求拦截器
 */
@Component
public class NewlandPropertyRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(NewlandPropertyRestTemplateInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.error("进入拦截器" + new String(body));
        TraceDto traceDto = NewlandPropertyTraceFactory.getTraceDto();
        if (traceDto != null) {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.remove(CommonConstant.TRACE_ID);
            httpHeaders.remove(CommonConstant.PARENT_SPAN_ID);
            httpHeaders.add(CommonConstant.TRACE_ID, traceDto.getTraceId());
            httpHeaders.add(CommonConstant.PARENT_SPAN_ID, traceDto.getId());
        }
        NewlandPropertyTraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        ClientHttpResponse clientHttpResponse = execution.execute(request, body);
        NewlandPropertyTraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
        return clientHttpResponse;
    }
}
