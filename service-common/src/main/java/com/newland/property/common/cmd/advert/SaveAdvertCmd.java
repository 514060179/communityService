/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.common.cmd.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileDto;
import com.newland.property.intf.common.IAdvertItemV1InnerServiceSMO;
import com.newland.property.intf.common.IAdvertV1InnerServiceSMO;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.po.advert.AdvertItemPo;
import com.newland.property.po.advert.AdvertPo;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@NewlandPropertyCmdDoc(title = "发布广告",
        description = "主要用于给业主端物业员工端发送广告",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/advert.saveAdvert",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "advert.saveAdvert"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "adName", length = 64, remark = "广告名称不能为空"),
        @NewlandPropertyParamDoc(name = "adTypeCd", length = 12, remark = "投放位置 2000 业主首页 4000 员工首页"),
        @NewlandPropertyParamDoc(name = "classify", length = 12, remark = "广告分类，9001 物流 9002 餐饮 9003 旅游 "),
        @NewlandPropertyParamDoc(name = "locationTypeCd", length = 12, remark = "固定值 1000 "),
        @NewlandPropertyParamDoc(name = "locationObjId", length = 12, remark = "固定值 -1 "),
        @NewlandPropertyParamDoc(name = "seq", length = 12, remark = "顺序 "),
        @NewlandPropertyParamDoc(name = "startTime", length = 12, remark = "开始时间 YYYY-MM-DD "),
        @NewlandPropertyParamDoc(name = "endTime", length = 12, remark = "结束时间 YYYY-MM-DD"),
        @NewlandPropertyParamDoc(name = "photos", length = 1024, remark = "图片"),

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"advertId\":\"\",\"adName\":\"123\",\"adTypeCd\":\"20000\",\"classify\":\"9001\",\"locationTypeCd\":\"1000\",\"locationObjId\":\"-1\",\"seq\":\"1\",\"startTime\":\"2022-09-01 00:00:00\",\"endTime\":\"2022-12-14 05:25:00\",\"advertType\":\"1\",\"pageUrl\":\"11\",\"floorId\":\"\",\"floorNum\":\"\",\"floorName\":\"\",\"unitId\":\"\",\"unitName\":\"\",\"roomId\":\"\",\"photos\":[{\"url\":\"https://jinlintong.oss-cn-zhangjiakou.aliyuncs.com/hc/img/20220919/223ef6f0-5207-47f1-88cf-99deaad482ce.jpg\",\"fileId\":\"img/20220919/223ef6f0-5207-47f1-88cf-99deaad482ce.jpg\"}],\"viewType\":\"8888\",\"vedioName\":\"\"}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：保存
 * 服务编码：advert.saveAdvert
 * 请求路劲：/app/advert.SaveAdvert
 * add by 吴学文 at 2022-06-19 10:07:43 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "advert.saveAdvert")
public class SaveAdvertCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveAdvertCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IAdvertV1InnerServiceSMO advertV1InnerServiceSMOImpl;

    @Autowired
    private IAdvertItemV1InnerServiceSMO advertItemV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "adName", "必填，请填写广告名称");
        Assert.hasKeyAndValue(reqJson, "adTypeCd", "必填，请选择广告类型");
        Assert.hasKeyAndValue(reqJson, "classify", "必填，请选择广告分类");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，请选择投放位置");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，请填写具体位置");
        //Assert.hasKeyAndValue(reqJson, "state", "必填，请填写广告状态");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写播放顺序");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择投放时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "viewType", "必填，请选择类型");
        if (!hasKeyAndValue(reqJson, "photos") && !hasKeyAndValue(reqJson, "vedioName")) {
            throw new IllegalArgumentException("请求报文中没有包含视频或图片");
        }

    }

    private boolean hasKeyAndValue(JSONObject paramIn, String key) {
        if (!paramIn.containsKey(key)) {
            return false;
        }
        if (StringUtil.isEmpty(paramIn.getString(key))) {
            return false;
        }
        return true;
    }


    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String advertId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_advertId);
        reqJson.put("advertId", advertId);
        reqJson.put("state", "1000");
        reqJson.put("createTime", new Date());
        reqJson.put("communityId", "9999");
        AdvertPo advertPo = BeanConvertUtil.covertBean(reqJson, AdvertPo.class);
        int flag = advertV1InnerServiceSMOImpl.saveAdvert(advertPo);
        if(flag < 1){
            throw new CmdException("保存失败");
        }
        if (hasKeyAndValue(reqJson, "photos") && reqJson.getJSONArray("photos").size() > 0) {
            JSONArray photos = reqJson.getJSONArray("photos");
            for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
                addAdvertItemPhoto(reqJson, cmdDataFlowContext, photos.getString(_photoIndex));
                addAdvertFileRel(reqJson, cmdDataFlowContext, "40000");
            }
        } else {
            addAdvertItemVedio(reqJson, cmdDataFlowContext);
            addAdvertFileRel(reqJson, cmdDataFlowContext, "50000");
        }
    }

    public void addAdvertItemPhoto(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String photo) {
        String itemTypeCd = "";
        String url = "";
        if(photo.length()> 512) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(photo);
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(paramInJson.getString("communityId"));
            photo = fileInnerServiceSMOImpl.saveFile(fileDto);
        }
        paramInJson.put("fileSaveName", photo);
        paramInJson.put("advertPhotoId", photo);
        itemTypeCd = "8888";
        /*        url = fileDto.getFileId();*/
        AdvertItemPo advertItemPo = new AdvertItemPo();
        advertItemPo.setAdvertId(paramInJson.getString("advertId"));
        advertItemPo.setAdvertItemId("-1");
        advertItemPo.setCommunityId(paramInJson.getString("communityId"));
        advertItemPo.setItemTypeCd(itemTypeCd);
        advertItemPo.setUrl(photo);
        advertItemPo.setSeq("1");
        int flag = advertItemV1InnerServiceSMOImpl.saveAdvertItem(advertItemPo);
        if(flag < 1){
            throw new CmdException("保存广告失败");
        }
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertFileRel(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String relTypeCd) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setRelTypeCd(relTypeCd);
        fileRelPo.setSaveWay("40000".equals(relTypeCd) ? "table" : "ftp");
        fileRelPo.setFileRelId("-1");
        fileRelPo.setObjId(paramInJson.getString("advertId"));
        fileRelPo.setFileRealName(paramInJson.getString("vedioName"));
        fileRelPo.setFileSaveName(paramInJson.getString("vedioName"));
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if(flag < 1){
            throw new CmdException("保存广告失败");
        }
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAdvertItemVedio(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        fileRelPo.setObjId(paramInJson.getString("advertId"));
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //50000 广告视频
        fileRelPo.setRelTypeCd("50000");
        fileRelPo.setFileRealName(paramInJson.getString("vedioName"));
        fileRelPo.setFileSaveName(paramInJson.getString("vedioName"));
        fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        /*String itemTypeCd = "9999";
        String url = paramInJson.getString("vedioName");
        paramInJson.put("advertPhotoId", url);
        AdvertItemPo advertItemPo = new AdvertItemPo();
        advertItemPo.setAdvertId(paramInJson.getString("advertId"));
        advertItemPo.setAdvertItemId("-1");
        advertItemPo.setCommunityId(paramInJson.getString("communityId"));
        advertItemPo.setItemTypeCd(itemTypeCd);
        advertItemPo.setUrl(url);
        advertItemPo.setSeq("1");
        super.insert(dataFlowContext, advertItemPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT_ITEM);*/
    }
}
