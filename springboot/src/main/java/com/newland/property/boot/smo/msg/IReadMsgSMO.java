package com.newland.property.boot.smo.msg;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 查询 消息
 */
public interface IReadMsgSMO {

    /**
     * 阅读消息
     * @param pd 上下文对象
     * @return
     */
    ResponseEntity<String> readMsg(IPageData pd);
}
