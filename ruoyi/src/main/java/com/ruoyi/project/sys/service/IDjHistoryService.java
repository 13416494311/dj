package com.ruoyi.project.sys.service;

import java.util.List;
import com.ruoyi.project.sys.domain.DjHistory;

/**
 * 党史Service接口
 * 
 * @author ruoyi
 * @date 2020-10-27
 */
public interface IDjHistoryService 
{
    /**
     * 查询党史
     * 
     * @param historyId 党史ID
     * @return 党史
     */
    public DjHistory selectDjHistoryById(Long historyId);

    /**
     * 查询党史列表
     * 
     * @param djHistory 党史
     * @return 党史集合
     */
    public List<DjHistory> selectDjHistoryList(DjHistory djHistory);

    /**
     * 新增党史
     * 
     * @param djHistory 党史
     * @return 结果
     */
    public int insertDjHistory(DjHistory djHistory);

    /**
     * 修改党史
     * 
     * @param djHistory 党史
     * @return 结果
     */
    public int updateDjHistory(DjHistory djHistory);

    /**
     * 批量删除党史
     * 
     * @param historyIds 需要删除的党史ID
     * @return 结果
     */
    public int deleteDjHistoryByIds(Long[] historyIds);

    /**
     * 删除党史信息
     * 
     * @param historyId 党史ID
     * @return 结果
     */
    public int deleteDjHistoryById(Long historyId);
}
