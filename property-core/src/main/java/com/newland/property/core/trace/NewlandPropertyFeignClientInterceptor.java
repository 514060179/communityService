package com.newland.property.core.trace;

import com.newland.property.dto.trace.TraceAnnotationsDto;
import com.newland.property.dto.trace.TraceDto;
import com.newland.property.utils.constant.CommonConstant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

//@Component
public class NewlandPropertyFeignClientInterceptor implements Interceptor {
    // private static Logger logger = LoggerFactory.getLogger(NewlandPropertyFeignClientInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Request request = chain.request();

        Request.Builder builder = chain.request().newBuilder();
        //调用链头信息
        TraceDto traceDto = NewlandPropertyTraceFactory.getTraceDto();
        if (traceDto != null) {
            builder.header(CommonConstant.TRACE_ID, traceDto.getTraceId());
            builder.header(CommonConstant.PARENT_SPAN_ID, traceDto.getId());
        }
        //logger.debug("feign 进入 NewlandPropertyFeignClientAop>> intercept");
        NewlandPropertyTraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        try {
            Response response = chain.proceed(builder.build());
            //after
            return response;
        } catch (Exception e) {
            //log error
            throw e;
        } finally {
            //clean up
            NewlandPropertyTraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
        }
    }


}
