package com.newland.property.order.listener;

import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.core.context.IOrderDataFlowContext;
import com.newland.property.core.event.app.order.Ordered;
import com.newland.property.core.event.center.event.InvokeBusinessSystemEvent;
import com.newland.property.core.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

/**
 * 用户ID处理 侦听
 * Created by wuxw on 2018/7/2.
 */
//@Component
@NewlandPropertyListener(name="dealUserIdListener")
public class DealUserIdListener implements DataFlowListener<InvokeBusinessSystemEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(DealUserIdListener.class);

    @Override
    public void soService(InvokeBusinessSystemEvent event) {
       IOrderDataFlowContext dataFlow = event.getDataFlow();
       if (dataFlow == null || dataFlow.getBusinessList() == null || dataFlow.getBusinessList().size() == 0){
           throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"用户ID处理侦听执行异常，没有可处理的business");
       }

       //判断是否存在 "serviceCode": "save.user.info", 业务，如果存在则处理
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
