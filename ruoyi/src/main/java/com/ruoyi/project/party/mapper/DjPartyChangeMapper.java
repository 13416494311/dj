package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyChange;

/**
 * 党组织换届Mapper接口
 *
 * @author ruoyi
 * @date 2021-02-18
 */
public interface DjPartyChangeMapper
{
    /**
     * 查询党组织换届
     *
     * @param changeId 党组织换届ID
     * @return 党组织换届
     */
    public DjPartyChange selectDjPartyChangeById(Long changeId);

    /**
     * 查询党组织换届列表
     *
     * @param djPartyChange 党组织换届
     * @return 党组织换届集合
     */
    public List<DjPartyChange> selectDjPartyChangeList(DjPartyChange djPartyChange);

    /**
     * 新增党组织换届
     *
     * @param djPartyChange 党组织换届
     * @return 结果
     */
    public int insertDjPartyChange(DjPartyChange djPartyChange);

    /**
     * 修改党组织换届
     *
     * @param djPartyChange 党组织换届
     * @return 结果
     */
    public int updateDjPartyChange(DjPartyChange djPartyChange);

    /**
     * 删除党组织换届
     *
     * @param changeId 党组织换届ID
     * @return 结果
     */
    public int deleteDjPartyChangeById(Long changeId);

    /**
     * 批量删除党组织换届
     *
     * @param changeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyChangeByIds(Long[] changeIds);
}
