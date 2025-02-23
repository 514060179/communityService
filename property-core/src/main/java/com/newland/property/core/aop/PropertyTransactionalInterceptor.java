package com.newland.property.core.aop;

import com.newland.property.core.factory.PropertyTransactionalFactory;
import com.newland.property.dto.order.OrderDto;
import com.newland.property.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 写上层事务ID
 */
@Component
public class PropertyTransactionalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //说明已经开启事务
        if (!StringUtils.isEmpty(PropertyTransactionalFactory.getOId())) {
            return true;
        }
        String oId = request.getHeader(OrderDto.O_ID);

        if (!StringUtil.isEmpty(oId)) {
            PropertyTransactionalFactory.put(PropertyTransactionalFactory.O_ID, oId);
        }

        return true;
    }
}
