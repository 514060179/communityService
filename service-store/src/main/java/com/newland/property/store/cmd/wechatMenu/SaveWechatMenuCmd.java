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
package com.newland.property.store.cmd.wechatMenu;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.wechat.WechatMenuDto;
import com.newland.property.intf.store.IWechatMenuInnerServiceSMO;
import com.newland.property.intf.store.IWechatMenuV1InnerServiceSMO;
import com.newland.property.po.wechat.WechatMenuPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：wechatMenu.saveWechatMenu
 * 请求路劲：/app/wechatMenu.SaveWechatMenu
 * add by 吴学文 at 2022-07-29 11:48:22 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "smallWeChat.saveWechatMenu")
public class SaveWechatMenuCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveWechatMenuCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IWechatMenuV1InnerServiceSMO wechatMenuV1InnerServiceSMOImpl;

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "menuName", "请求报文中未包含menuName");
        Assert.hasKeyAndValue(reqJson, "menuType", "请求报文中未包含menuType");
        Assert.hasKeyAndValue(reqJson, "menuLevel", "请求报文中未包含menuLevel");
        //Assert.hasKeyAndValue(reqJson, "menuValue", "请求报文中未包含menuValue");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含顺序");
        Assert.isInteger(reqJson.getString("seq"),"顺序不是有效数字");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        String menuName = reqJson.getString("menuName");

        if (WechatMenuDto.MENU_LEVEL_ONE.equals(reqJson.getString("menuLevel"))) {
            // 名字不能超过
            if (menuName.length() > 4 && menuName.length() < 1) {
                throw new IllegalArgumentException("一级菜单必须1至4位");
            }
            //查询 一级菜单是否操作3个
            WechatMenuDto wechatMenuDto = new WechatMenuDto();
            wechatMenuDto.setCommunityId(reqJson.getString("communityId"));
            wechatMenuDto.setMenuLevel(reqJson.getString("menuLevel"));
            List<WechatMenuDto> wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
            if(wechatMenuDtos.size() > 2){
                throw new IllegalArgumentException("一级菜单最多3个");
            }
        } else {
            Assert.hasKeyAndValue(reqJson, "parentMenuId", "未包含一级菜单信息");
            if (menuName.length() > 7 && menuName.length() < 1) {
                throw new IllegalArgumentException("二级菜单必须1至7位");
            }

            WechatMenuDto wechatMenuDto = new WechatMenuDto();
            wechatMenuDto.setCommunityId(reqJson.getString("communityId"));
            wechatMenuDto.setMenuLevel(reqJson.getString("menuLevel"));
            wechatMenuDto.setParentMenuId(reqJson.getString("parentMenuId"));
            List<WechatMenuDto> wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
            if(wechatMenuDtos.size() > 4){
                throw new IllegalArgumentException("二级菜单最多5个");
            }
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(reqJson, WechatMenuPo.class);
        wechatMenuPo.setWechatMenuId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = wechatMenuV1InnerServiceSMOImpl.saveWechatMenu(wechatMenuPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
