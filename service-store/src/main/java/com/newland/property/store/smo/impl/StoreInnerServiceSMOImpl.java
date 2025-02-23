package com.newland.property.store.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.store.StoreAttrDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.store.StorePo;
import com.newland.property.store.dao.IStoreServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StoreInnerServiceSMOImpl 商户内部实现类
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/20 15:17
 * @Version 1.0
 * add by wuxw 2019/9/20
 **/
@RestController
public class StoreInnerServiceSMOImpl extends BaseServiceSMO implements IStoreInnerServiceSMO {

    @Autowired
    private IStoreServiceDao storeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<StoreDto> getStores(@RequestBody StoreDto storeDto) {
        //校验是否传了 分页信息

        int page = storeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeDto.setPage((page - 1) * storeDto.getRow());
        }

        List<StoreDto> storeDtos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStores(BeanConvertUtil.beanCovertMap(storeDto)), StoreDto.class);

        if (storeDtos == null || storeDtos.size() == 0) {
            return storeDtos;
        }

      /*  String[] userIds = getUserIds(ownerCars);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerCarDto ownerCar : ownerCars) {
            refreshOwnerCar(ownerCar, users);
        }*/
        return storeDtos;
    }

    @Override
    public List<StoreAttrDto> getStoreAttrs(@RequestBody StoreAttrDto storeAttrDto) {
        List<StoreAttrDto> storeAttrDtos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStoreAttrs(BeanConvertUtil.beanCovertMap(storeAttrDto)), StoreAttrDto.class);
        return storeAttrDtos;
    }


    @Override
    public int getStoreCount(@RequestBody StoreDto storeDto) {
        return storeServiceDaoImpl.getStoreCount(BeanConvertUtil.beanCovertMap(storeDto));
    }

    @Override
    public int updateStore(@RequestBody StorePo storePo) {
        return storeServiceDaoImpl.updateStore(BeanConvertUtil.beanCovertMap(storePo));
    }

    /**
     * 查询员工和员工所属商户信息
     *
     * @param storeUserDto
     * @return
     */
    @Override
    public List<StoreUserDto> getStoreUserInfo(@RequestBody StoreUserDto storeUserDto) {
        List<StoreUserDto> storeUserInfos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStoreUserInfo(BeanConvertUtil.beanCovertMap(storeUserDto)), StoreUserDto.class);
        return storeUserInfos;
    }

    @Override
    public int getStoreStaffCount(@RequestBody StoreUserDto storeUserDto) {
        return storeServiceDaoImpl.getStoreStaffCount(BeanConvertUtil.beanCovertMap(storeUserDto));
    }
    /**
     * 查询商户员工信息
     *
     * @param storeUserDto
     * @return
     */
    @Override
    public List<StoreUserDto> getStoreStaffs(StoreUserDto storeUserDto) {
        int page = storeUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeUserDto.setPage((page - 1) * storeUserDto.getRow());
        }

        List<StoreUserDto> storeUserDtos = BeanConvertUtil.covertBeanList(
                storeServiceDaoImpl.getStoreStaffs(BeanConvertUtil.beanCovertMap(storeUserDto)), StoreUserDto.class);

        if (storeUserDtos == null || storeUserDtos.size() == 0) {
            return storeUserDtos;
        }

        String[] userIds = getUserIds(storeUserDtos);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (StoreUserDto tmpStoreUserDto : storeUserDtos) {
            for(UserDto userDto : users){
                if(tmpStoreUserDto.getStaffId().equals(userDto.getUserId())){
                    tmpStoreUserDto.setStaffName(userDto.getName());
                    tmpStoreUserDto.setStaffTel(userDto.getTel());
                }
            }
        }
        return storeUserDtos;
    }

    private String[] getUserIds(List<StoreUserDto> storeUserDtos) {

        List<String> userIds = new ArrayList<>();
        for (StoreUserDto storeUserDto : storeUserDtos) {
            userIds.add(storeUserDto.getStaffId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }


    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
