package com.newland.property.common.cmd.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.advert.AdvertDto;
import com.newland.property.dto.advert.AdvertItemDto;
import com.newland.property.intf.common.IAdvertInnerServiceSMO;
import com.newland.property.intf.common.IAdvertItemInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "advert.listAdvertPhoto")
public class ListAdvertPhotoCmd extends Cmd {

    @Autowired
    private IAdvertInnerServiceSMO advertInnerServiceSMOImpl;

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;


        JSONArray advertPhoto = new JSONArray();


        //如果是大门 则只获取小区的广告
        AdvertDto advertDto = BeanConvertUtil.covertBean(reqJson, AdvertDto.class);
        if(!StringUtil.isEmpty("clientType") && "H5".equals(reqJson.get("clientType"))){
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            advertDto.setStartTime(df.format(day));
            advertDto.setEndTime(df.format(day));
        }
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhoto);
        }

        responseEntity = new ResponseEntity<String>(advertPhoto.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private boolean getCommunityAdvert(String communityId, JSONArray advertPhotoAndVideos) {
        AdvertDto advertDto = new AdvertDto();
        advertDto.setCommunityId(communityId);
        advertDto.setLocationObjId(communityId);
        List<AdvertDto> advertDtos = advertInnerServiceSMOImpl.queryAdverts(advertDto);

        if (advertDtos != null && advertDtos.size() != 0) {
            this.getAdvertItem(advertDtos, advertPhotoAndVideos);
            return true;
        }

        return false;
    }

    /**
     * 查询广告照片
     *
     * @param advertDtos
     * @param advertPhotoAndVideos
     */
    private void getAdvertItem(List<AdvertDto> advertDtos, JSONArray advertPhotoAndVideos) {
        JSONObject photoAndVideo = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for (AdvertDto advertDto : advertDtos) {

            AdvertItemDto advertItemDto = new AdvertItemDto();
            advertItemDto.setAdvertId(advertDto.getAdvertId());
            advertItemDto.setCommunityId(advertDto.getCommunityId());
            List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);

            for (AdvertItemDto tmpAdvertItemDto : advertItemDtos) {
                //照片
                if (!"8888".equals(tmpAdvertItemDto.getItemTypeCd())) {
                    continue;
                }

                photoAndVideo = new JSONObject();
                photoAndVideo.put("suffix", "JPEG");
                //photoAndVideo.put("url", "/callComponent/download/getFile/file?fileId=" + tmpAdvertItemDto.getUrl() + "&communityId=" + advertDto.getCommunityId());
                if(tmpAdvertItemDto.getUrl().startsWith("http")){
                    photoAndVideo.put("url", tmpAdvertItemDto.getUrl());
                }else{
                    photoAndVideo.put("url", imgUrl + tmpAdvertItemDto.getUrl());
                }
                photoAndVideo.put("seq", tmpAdvertItemDto.getSeq());
                photoAndVideo.put("advertType", advertDto.getAdvertType());
                photoAndVideo.put("pageUrl", advertDto.getPageUrl());
                advertPhotoAndVideos.add(photoAndVideo);
            }
        }

    }
}
