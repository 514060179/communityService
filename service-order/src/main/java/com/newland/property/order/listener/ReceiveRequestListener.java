package com.newland.property.order.listener;

import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.core.event.app.order.Ordered;
import com.newland.property.core.event.center.event.ReceiveRequestEvent;
import com.newland.property.core.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

import java.util.Map;

/**
 * Created by wuxw on 2018/7/2.
 */
@NewlandPropertyListener(name = "receiveRequestListener")
public class ReceiveRequestListener implements DataFlowListener<ReceiveRequestEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveRequestListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void soService(ReceiveRequestEvent event) {

        Map<String,String> headers = event.getHeaders();
        logger.debug("请求头信息为：{}",headers);
        String requestData = event.getRequestData();
        logger.debug("请求报文为：{}",requestData);
    }
}
