package com.newland.property.api.components.fee;


import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.fee.IListFeeConfigsSMO;
import com.newland.property.api.smo.fee.IRoomCreateFeeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 费用项组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("roomCreateFeeAdd")
public class RoomCreateFeeAddComponent {

    @Autowired
    private IListFeeConfigsSMO listFeeConfigsSMOImpl;

    @Autowired
    private IRoomCreateFeeSMO roomCreateFeeSMOImpl;



    /**
     * 查询费用项列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listFeeConfigsSMOImpl.listFeeConfigs(pd);
    }

    /**
     * 批量创建费用
     * @param pd
     * @return
     */
    public ResponseEntity<String> save(IPageData pd) {
        return roomCreateFeeSMOImpl.createFee(pd);
    }

    public IListFeeConfigsSMO getListFeeConfigsSMOImpl() {
        return listFeeConfigsSMOImpl;
    }

    public void setListFeeConfigsSMOImpl(IListFeeConfigsSMO listFeeConfigsSMOImpl) {
        this.listFeeConfigsSMOImpl = listFeeConfigsSMOImpl;
    }

    public IRoomCreateFeeSMO getRoomCreateFeeSMOImpl() {
        return roomCreateFeeSMOImpl;
    }

    public void setRoomCreateFeeSMOImpl(IRoomCreateFeeSMO roomCreateFeeSMOImpl) {
        this.roomCreateFeeSMOImpl = roomCreateFeeSMOImpl;
    }
}
