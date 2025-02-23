package com.newland.property.report.bmo.search;

import com.newland.property.dto.data.SearchDataDto;

/**
 * 查询房屋信息
 */
public interface ISearchRoomBMO {

    /**
     * 查询房屋信息
     * @param searchDataDto
     * @return
     */
    SearchDataDto query(SearchDataDto searchDataDto);
}
