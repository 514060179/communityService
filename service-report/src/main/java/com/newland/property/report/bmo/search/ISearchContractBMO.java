package com.newland.property.report.bmo.search;

import com.newland.property.dto.data.SearchDataDto;

/**
 * 查询合同信息
 */
public interface ISearchContractBMO {

    /**
     * 查询房屋信息
     * @param searchDataDto
     * @return
     */
    SearchDataDto query(SearchDataDto searchDataDto);
}
