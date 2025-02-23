package com.newland.property.core.aop;

import com.newland.property.core.factory.PropertyTransactionalFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.order.OrderDto;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @ClassName PropertyTransactionalAop
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/3 22:13
 * @Version 1.0
 * add by wuxw 2020/7/3
 **/
@Component
@Aspect
public class PropertyTransactionalAop {

    private static Logger logger = LoggerFactory.getLogger(PropertyTransactionalAop.class);

    @Pointcut("@annotation(com.newland.property.core.annotation.PropertyTransactional)")
    public void dataProcess() {
    }

    /**
     * 初始化数据
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("dataProcess()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {

    }

    @AfterReturning(returning = "ret", pointcut = "dataProcess()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("方法调用前执行doAfterReturning（）");
    }

    //后置异常通知
    @AfterThrowing("dataProcess()")
    public void throwException(JoinPoint jp) {
        logger.debug("方法调用异常执行throwException（）");


    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("dataProcess()")
    public void after(JoinPoint jp) throws IOException {
        // 接收到请求，记录请求内容
        logger.debug("方法调用后执行after（）");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    /**
     * 环绕通知，用于在方法执行前后添加额外的逻辑处理。相当于AOP中的MethodInterceptor。
     * 其主要作用是通过拦截指定切面的数据处理过程，来进行一些额外的操作，例如事务管理、日志记录等。
     *
     * @param pjp ProceedingJoinPoint 对象，代表当前被拦截的方法的执行点。
     * @return 返回被拦截方法的执行结果。
     * @throws Throwable 如果执行过程中发生异常，则抛出。
     */
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object o = null;
        // 在方法执行前，记录请求内容。
        String curOId = PropertyTransactionalFactory.getOId();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 当前没有全局事务ID且存在请求属性时，从HTTP请求头中提取相关信息，并设置全局事务ID。
        if (StringUtil.isEmpty(curOId) && attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            OrderDto orderDto = new OrderDto();

            // 遍历请求头，提取并记录相关信息。
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                logger.debug("请求头信息 key= " + key + ",value = " + value);

                key = key.toLowerCase();
                // 根据请求头的键设置OrderDto的相关字段。
                if (CommonConstant.APP_ID.equals(key) || CommonConstant.HTTP_APP_ID.equals(key)) {
                    orderDto.setAppId(value);
                }
                if (CommonConstant.TRANSACTION_ID.equals(key) || CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                    orderDto.setExtTransactionId(value);
                }
                if (CommonConstant.REQUEST_TIME.equals(key) || CommonConstant.HTTP_REQ_TIME.equals(key)) {
                    orderDto.setRequestTime(value);
                }
                if (OrderDto.O_ID.equals(key)) {
                    orderDto.setoId(value);
                }
                if (CommonConstant.USER_ID.equals(key) || CommonConstant.HTTP_USER_ID.equals(key)) {
                    orderDto.setUserId(value);
                }
            }
            orderDto.setOrderTypeCd(OrderDto.ORDER_TYPE_DEAL);
            // 申请全局事务ID。
            PropertyTransactionalFactory.getOrCreateOId(orderDto);
        }

        try {
            // 执行被拦截的方法。
            o = pjp.proceed();
            // 如果当前角色为观察者，则直接返回结果不做进一步处理。
            if (PropertyTransactionalFactory.ROLE_OBSERVER.equals(PropertyTransactionalFactory.getServiceRole())) {
                return o;
            }
            // 在方法执行成功后，且存在全局事务ID的情况下，完成事务处理。
            if (StringUtil.isEmpty(curOId)  && attributes != null) {
                PropertyTransactionalFactory.finishOId();
            }
            return o;
        } catch (Throwable e) {
            // 如果方法执行发生异常，记录日志，并回退事务。
            logger.error("执行方法异常", e);
            //回退事务
            if (StringUtil.isEmpty(curOId)  && attributes != null) {
                PropertyTransactionalFactory.fallbackOId();
            }
            //return new BusinessDto(BusinessDto.CODE_ERROR, "内部异常" + e.getLocalizedMessage());
            throw e;
        } finally {
            // 在方法执行结束后（无论成功或失败），清理事务信息。
            if (StringUtil.isEmpty(curOId)) {
                //清理事务信息
                PropertyTransactionalFactory.clearOId();
            }
        }
    }
}
