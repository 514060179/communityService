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
package com.newland.property.dev.cmd.dict;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.dict.DictSpecDto;
import com.newland.property.intf.dev.IDictSpecV1InnerServiceSMO;
import com.newland.property.intf.dev.IDictV1InnerServiceSMO;
import com.newland.property.po.dict.DictPo;
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
 * 服务编码：dict.saveDict
 * 请求路劲：/app/dict.SaveDict
 * add by 吴学文 at 2022-07-06 20:03:58 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "dict.saveDict")
public class SaveDictCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveDictCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Autowired
    private IDictSpecV1InnerServiceSMO dictSpecV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "statusCd", "请求报文中未包含statusCd");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含类型");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        DictSpecDto dictSpecDto  = new DictSpecDto();
        dictSpecDto.setSpecId(reqJson.getString("specId"));
        List<DictSpecDto> dictSpecDtos = dictSpecV1InnerServiceSMOImpl.queryDictSpecs(dictSpecDto);

        Assert.listOnlyOne(dictSpecDtos,"未查询到类型");

        DictPo dictPo = BeanConvertUtil.covertBean(reqJson, DictPo.class);
        dictPo.setTableName(dictSpecDtos.get(0).getTableName());
        dictPo.setTableColumns(dictSpecDtos.get(0).getTableColumns());
        int flag = dictV1InnerServiceSMOImpl.saveDict(dictPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
