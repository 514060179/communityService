package com.newland.property.api.components.carBlackWhite;


import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.carBlackWhite.IListCarBlackWhitesSMO;
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
@Component("chooseCarBlackWhite")
public class ChooseCarBlackWhiteComponent {

    @Autowired
    private IListCarBlackWhitesSMO listCarBlackWhitesSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listCarBlackWhitesSMOImpl.listCarBlackWhites(pd);
    }

    public IListCarBlackWhitesSMO getListCarBlackWhitesSMOImpl() {
        return listCarBlackWhitesSMOImpl;
    }

    public void setListCarBlackWhitesSMOImpl(IListCarBlackWhitesSMO listCarBlackWhitesSMOImpl) {
        this.listCarBlackWhitesSMOImpl = listCarBlackWhitesSMOImpl;
    }
}
