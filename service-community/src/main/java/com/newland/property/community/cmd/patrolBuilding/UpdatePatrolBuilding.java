package com.newland.property.community.cmd.patrolBuilding;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.floor.FloorDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IFloorV1InnerServiceSMO;
import com.newland.property.intf.community.IPatrolBuildingV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.patrolBuilding.PatrolBuildingPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 修改巡楼信息
 *
 * @author fqz
 * @date 2023-12-26 16:22
 */
@NewlandPropertyCmd(serviceCode = "patrolBuilding.updatePatrolBuilding")
public class UpdatePatrolBuilding extends Cmd {

    @Autowired
    private IPatrolBuildingV1InnerServiceSMO iPatrolBuildingV1InnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "staffId", "请求报文中未包含员工ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        PatrolBuildingPo patrolBuildingPo = BeanConvertUtil.covertBean(reqJson, PatrolBuildingPo.class);
        //楼栋改变时，对应的楼栋编号也要改变
        if (reqJson.containsKey("floorId") && !StringUtil.isEmpty(reqJson.getString("floorId")) && !"-1".equals(reqJson.getString("floorId"))) {
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorId(reqJson.getString("floorId"));
            List<FloorDto> floorList = floorV1InnerServiceSMOImpl.queryFloors(floorDto);
            Assert.listOnlyOne(floorList, "查询楼栋错误！");
            patrolBuildingPo.setFloorNum(floorList.get(0).getFloorNum());
        } else {
            patrolBuildingPo.setFloorId("-1");
            patrolBuildingPo.setFloorNum("-1");
        }
        int flag = iPatrolBuildingV1InnerServiceSMOImpl.updatePatrolBuilding(patrolBuildingPo);
        if (flag < 1) {
            throw new CmdException("修改巡楼信息失败");
        }
        //处理图片
        JSONArray photos = reqJson.getJSONArray("photos");
        FileRelPo fileRel = new FileRelPo();
        fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        fileRel.setObjId(patrolBuildingPo.getPbId());
        //table表示表存储 ftp表示ftp文件存储
        fileRel.setSaveWay("ftp");
        fileRel.setCreateTime(new Date());
        //图片上传
        if (photos != null && photos.size() > 0) {
            //有图片传过来，先把原先的图片删除，重新上传图片信息
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setObjId(reqJson.getString("pbId"));
            fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
            //31000表示巡楼图片
            fileRel.setRelTypeCd("31000");
            for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
                JSONObject param = photos.getJSONObject(_photoIndex);
                fileRel.setFileRealName(param.getString("photo"));
                fileRel.setFileSaveName(param.getString("photo"));
                fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
            }
        }
    }
}
