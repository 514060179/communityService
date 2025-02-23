package com.newland.property.api.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.cache.NewlandPropertyRedisConfig;
import com.newland.property.core.context.IPageData;
import com.newland.property.core.context.SecureInvocation;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GetCommunityStoreInfoSMOImpl extends DefaultAbstractComponentSMO implements IGetCommunityStoreInfoSMO {

    private static final String BASE_PRIVILEGE = "NEWLANDPROPERTY_BASE_PRIVILEGE";

    @Override
    @Cacheable(value = "getStoreInfo" + NewlandPropertyRedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY, key = "#userId")
    public ResultVo getStoreInfo(IPageData pd, RestTemplate restTemplate, String userId) {


        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = super.callCenterService(restTemplate, pd, "", "query.store.byuser?userId=" + userId, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }

        if (!StringUtil.isJsonObject(responseEntity.getBody())) {
            // return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, responseEntity.getBody());
            throw new IllegalArgumentException(responseEntity.getBody());
        }

        JSONObject paramJson = JSONObject.parseObject(responseEntity.getBody());
        if (paramJson.containsKey("code") && paramJson.getIntValue("code") != 0) {
            throw new IllegalArgumentException(paramJson.getString("msg"));
        }

        return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, responseEntity.getBody());
    }

    @Override
    @Cacheable(value = "getStoreEnterCommunitys" + NewlandPropertyRedisConfig.GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY, key = "#storeId")
    public ResultVo getStoreEnterCommunitys(IPageData pd, String storeId, String storeTypeCd, RestTemplate restTemplate) {
        ResponseEntity<String> responseEntity = null;
//        responseEntity = CallApiServiceFactory.callCenterService(restTemplate, pd, "",
//                "query.myCommunity.byMember?memberId=" + storeId + "&memberTypeCd="
//                        + MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeTypeCd), HttpMethod.GET);

        responseEntity = super.callCenterService(restTemplate, pd, "",
                "/communitys/queryStoreCommunitys?memberId=" + storeId + "&memberTypeCd="
                        + MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeTypeCd), HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        if (paramOut.containsKey("code") && ResultVo.CODE_OK != paramOut.getIntValue("code")) {
            throw new IllegalArgumentException(paramOut.getString("msg"));
        }
        return new ResultVo(paramOut.getIntValue("code"), paramOut.getString("msg"), paramOut.get("data"));
    }

    @Override
    @Cacheable(value = "getUserPrivileges" + NewlandPropertyRedisConfig.DEFAULT_EXPIRE_TIME_KEY, key = "#staffId")
    public ResultVo getUserPrivileges(IPageData pd, String staffId, String storeTypeCd, RestTemplate restTemplate) {

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, "",
                "query.user.privilege?userId=" + staffId + "&domain=" + storeTypeCd, HttpMethod.GET);
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(privilegeGroup.getBody());
        }
        return new ResultVo(privilegeGroup.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, privilegeGroup.getBody());

    }

    @Override
    @Cacheable(value = "checkUserHasResourceListener" + NewlandPropertyRedisConfig.DEFAULT_EXPIRE_TIME_KEY, key = "#cacheKey")
    public ResultVo checkUserHasResourceListener(RestTemplate restTemplate, IPageData pd, JSONObject paramIn, String cacheKey) {
        ResponseEntity<String> responseEntity = null;
        responseEntity = super.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "basePrivilege.CheckUserHasResourceListener",
                HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }

        JSONObject data = JSONObject.parseObject(responseEntity.getBody());

        JSONArray privileges = data.getJSONArray("privileges");

        if (!SecureInvocation.secure(this.getClass())) {
            return new ResultVo(ResultVo.CODE_OK, privileges.toJSONString(), ResultVo.EMPTY_ARRAY);
        }

        return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, privileges.toJSONString());

    }
}
