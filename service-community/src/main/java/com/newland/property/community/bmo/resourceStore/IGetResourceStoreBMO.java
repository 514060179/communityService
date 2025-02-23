package com.newland.property.community.bmo.resourceStore;

import com.newland.property.po.purchase.ResourceStorePo;
import org.springframework.http.ResponseEntity;

/**
 * 查询资源表物品信息
 *
 * @author fqz
 * @date 2021-03-17 11:22
 */
public interface IGetResourceStoreBMO {

    /**
     * 查询资源表物品信息
     *
     * @param resourceStorePo
     * @return
     */
    ResponseEntity<String> get(ResourceStorePo resourceStorePo);

}
