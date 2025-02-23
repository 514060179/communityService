package com.newland.property.api.components.unit;


import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("chooseUnit")
public class ChooseUnitComponent {

    @Autowired
    private IUnitServiceSMO listUnitsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listUnitsSMOImpl.listUnits(pd);
    }


    public IUnitServiceSMO getListUnitsSMOImpl() {
        return listUnitsSMOImpl;
    }

    public void setListUnitsSMOImpl(IUnitServiceSMO listUnitsSMOImpl) {
        this.listUnitsSMOImpl = listUnitsSMOImpl;
    }
}
