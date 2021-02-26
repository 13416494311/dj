package com.ruoyi.project.members.mapper;

import java.util.List;
import com.ruoyi.project.members.domain.DjPartyExemplary;

/**
 * 先锋模范Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-26
 */
public interface DjPartyExemplaryMapper 
{
    /**
     * 查询先锋模范
     * 
     * @param exemplaryId 先锋模范ID
     * @return 先锋模范
     */
    public DjPartyExemplary selectDjPartyExemplaryById(Long exemplaryId);

    /**
     * 查询先锋模范列表
     * 
     * @param djPartyExemplary 先锋模范
     * @return 先锋模范集合
     */
    public List<DjPartyExemplary> selectDjPartyExemplaryList(DjPartyExemplary djPartyExemplary);

    /**
     * 新增先锋模范
     * 
     * @param djPartyExemplary 先锋模范
     * @return 结果
     */
    public int insertDjPartyExemplary(DjPartyExemplary djPartyExemplary);

    /**
     * 修改先锋模范
     * 
     * @param djPartyExemplary 先锋模范
     * @return 结果
     */
    public int updateDjPartyExemplary(DjPartyExemplary djPartyExemplary);

    /**
     * 删除先锋模范
     * 
     * @param exemplaryId 先锋模范ID
     * @return 结果
     */
    public int deleteDjPartyExemplaryById(Long exemplaryId);

    /**
     * 批量删除先锋模范
     * 
     * @param exemplaryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyExemplaryByIds(Long[] exemplaryIds);
}
