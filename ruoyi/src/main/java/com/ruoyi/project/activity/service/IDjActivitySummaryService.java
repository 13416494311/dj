package com.ruoyi.project.activity.service;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivitySummary;

/**
 * 活动纪要Service接口
 * 
 * @author ruoyi
 * @date 2020-08-22
 */
public interface IDjActivitySummaryService 
{
    /**
     * 查询活动纪要
     * 
     * @param summaryId 活动纪要ID
     * @return 活动纪要
     */
    public DjActivitySummary selectDjActivitySummaryById(Long summaryId);

    /**
     * 查询活动纪要列表
     * 
     * @param djActivitySummary 活动纪要
     * @return 活动纪要集合
     */
    public List<DjActivitySummary> selectDjActivitySummaryList(DjActivitySummary djActivitySummary);

    /**
     * 新增活动纪要
     * 
     * @param djActivitySummary 活动纪要
     * @return 结果
     */
    public int insertDjActivitySummary(DjActivitySummary djActivitySummary);

    /**
     * 修改活动纪要
     * 
     * @param djActivitySummary 活动纪要
     * @return 结果
     */
    public int updateDjActivitySummary(DjActivitySummary djActivitySummary);

    /**
     * 批量删除活动纪要
     * 
     * @param summaryIds 需要删除的活动纪要ID
     * @return 结果
     */
    public int deleteDjActivitySummaryByIds(Long[] summaryIds);

    /**
     * 删除活动纪要信息
     * 
     * @param summaryId 活动纪要ID
     * @return 结果
     */
    public int deleteDjActivitySummaryById(Long summaryId);
}
