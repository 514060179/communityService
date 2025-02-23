package com.newland.property.boot.smo;

import com.newland.property.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * Created by Administrator on 2019/4/1.
 */
public interface IMenuServiceSMO {

    /**
     * 根据用户查菜单
     * @return
     */
     ResponseEntity<String> queryMenusByUserId(IPageData pd);

}
