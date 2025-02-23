package com.newland.property.core.event.center.event;

import com.newland.property.core.context.IOrderDataFlowContext;
import com.newland.property.dto.system.Business;

/**
 * 调用业务系统事件
 * Created by wuxw on 2018/7/2.
 */
public class InvokeBusinessISuccessEvent extends DataFlowEvent {

    private Business business;

    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeBusinessISuccessEvent(Object source, IOrderDataFlowContext dataFlow, Business business) {
        super(source, dataFlow);
        this.business = business;
    }

    public Business getBusiness() {
        return business;
    }
}
