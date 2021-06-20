package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentPerformanceScore;

/**
 * 绩效考核评分Mapper接口
 *
 * @author ruoyi
 * @date 2021-06-20
 */
public interface DjOrgAssessmentPerformanceScoreMapper
{
    /**
     * 查询绩效考核评分
     *
     * @param id 绩效考核评分ID
     * @return 绩效考核评分
     */
    public DjOrgAssessmentPerformanceScore selectDjOrgAssessmentPerformanceScoreById(Long id);

    /**
     * 查询绩效考核评分列表
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 绩效考核评分集合
     */
    public List<DjOrgAssessmentPerformanceScore> selectDjOrgAssessmentPerformanceScoreList(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore);

    /**
     * 新增绩效考核评分
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 结果
     */
    public int insertDjOrgAssessmentPerformanceScore(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore);

    /**
     * 修改绩效考核评分
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 结果
     */
    public int updateDjOrgAssessmentPerformanceScore(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore);

    /**
     * 删除绩效考核评分
     *
     * @param id 绩效考核评分ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentPerformanceScoreById(Long id);

    /**
     * 批量删除绩效考核评分
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentPerformanceScoreByIds(Long[] ids);

    public List<String> getScoreItem(String assessmentYearUuid);
}
