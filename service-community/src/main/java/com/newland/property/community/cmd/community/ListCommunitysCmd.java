package com.newland.property.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.area.AreaDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.intf.common.IAreaInnerServiceSMO;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.community.ApiCommunityDataVo;
import com.newland.property.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
@NewlandPropertyCmdDoc(title = "查询小区",
        description = "查询系统中的所有小区信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/community.listCommunitys",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "community.listCommunitys",
        seq = 4
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "page",type = "int",length = 11, remark = "分页页数"),
        @NewlandPropertyParamDoc(name = "row",type = "int", length = 11, remark = "分页行数"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "communitys", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "communitys",name = "communityId", type = "String", remark = "小区ID"),
                @NewlandPropertyParamDoc(parentNodeName = "communitys",name = "name", type = "String", remark = "小区名称"),
                @NewlandPropertyParamDoc(parentNodeName = "communitys",name = "state", type = "String", remark = "小区状态 1100 审核完成"),
                @NewlandPropertyParamDoc(parentNodeName = "communitys",name = "cityCode", type = "String", remark = "小区状态 城市编码"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/community.listCommunitys?name=&cityCode=&communityId=&page=1&row=10",
        resBody="{\"communitys\":[{\"address\":\"青海省西宁市城西区国投广场\",\"cityCode\":\"630104\",\"cityName\":\"青海省西宁市城西区\",\"communityAttrDtos\":[{\"attrId\":\"112022081566950487\",\"communityId\":\"2022081539020475\",\"listShow\":\"Y\",\"page\":-1,\"records\":0,\"row\":0,\"specCd\":\"9329000004\",\"specName\":\"社区编码\",\"statusCd\":\"0\",\"total\":0,\"value\":\"123456\"}],\"communityId\":\"2022081539020475\",\"mapX\":\"101.33\",\"mapY\":\"101.33\",\"name\":\"测试小区\",\"nearbyLandmarks\":\"国投广场\",\"state\":\"1100\",\"stateName\":\"审核完成\",\"tel\":\"18909711443\"}],\"page\":0,\"records\":1,\"rows\":0,\"total\":2}"
)
@NewlandPropertyCmd(serviceCode = "community.listCommunitys")
public class ListCommunitysCmd extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);

        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<ApiCommunityDataVo> communitys = null;
        ApiCommunityDataVo apiCommunityDataVo = null;
        if (count > 0) {
            communitys = new ArrayList<>();
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            for (CommunityDto tmpCommunityDto : communityDtos) {
                apiCommunityDataVo = BeanConvertUtil.covertBean(tmpCommunityDto, ApiCommunityDataVo.class);
                apiCommunityDataVo.setCommunityAttrDtos(tmpCommunityDto.getCommunityAttrDtos());
                communitys.add(apiCommunityDataVo);
            }
            refreshCommunityCity(communitys);
        } else {
            communitys = new ArrayList<>();
        }
        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();
        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新cityName
     *
     * @param communitys
     */
    private void refreshCommunityCity(List<ApiCommunityDataVo> communitys) {
        List<String> areaCodes = new ArrayList<>();
        for (ApiCommunityDataVo communityDataVo : communitys) {
            areaCodes.add(communityDataVo.getCityCode());
        }
        if (areaCodes.size() > 0) {
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaCodes(areaCodes.toArray(new String[areaCodes.size()]));
            List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getProvCityArea(areaDto);
            for (ApiCommunityDataVo communityDataVo : communitys) {
                for (AreaDto tmpAreaDto : areaDtos) {
                    if (communityDataVo.getCityCode().equals(tmpAreaDto.getAreaCode())) {
                        communityDataVo.setCityName(tmpAreaDto.getProvName() + tmpAreaDto.getCityName() + tmpAreaDto.getAreaName());
                        break;
                    }
                    communityDataVo.setCityName("未知");
                }
            }
        }
    }
}
