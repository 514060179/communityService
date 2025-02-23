package com.newland.property.core.language;

import com.newland.property.dto.menu.MenuCatalogDto;
import com.newland.property.vo.ResultVo;

import java.util.List;
import java.util.Map;

public abstract class DefaultLanguage implements Language {





    @Override
    public List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos) {

        return menuCatalogDtos;
    }


    @Override
    public List<Map> getMenuDto(List<Map> menuDtos) {

        return menuDtos;
    }

    @Override
    public ResultVo getResultVo(ResultVo resultVo) {


        return resultVo;
    }

    @Override
    public String getLangMsg(String msg){


        return msg;
    }

}
