package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentListScore;

/**
 * 考核评价评分Mapper接口
 *
 * @author admin
 * @date 2021-03-12
 */
public interface DjOrgAssessmentListScoreMapper
{
    /**
     * 查询考核评价评分
     *
     * @param id 考核评价评分ID
     * @return 考核评价评分
     */
    public DjOrgAssessmentListScore selectDjOrgAssessmentListScoreById(Long id);

    /**
     * 查询考核评价评分列表
     *
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 考核评价评分集合
     */
    public List<DjOrgAssessmentListScore> selectDjOrgAssessmentListScoreList(DjOrgAssessmentListScore djOrgAssessmentListScore);

    /**
     * 新增考核评价评分
     *
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 结果
     */
    public int insertDjOrgAssessmentListScore(DjOrgAssessmentListScore djOrgAssessmentListScore);

    /**
     * 修改考核评价评分
     *
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 结果
     */
    public int updateDjOrgAssessmentListScore(DjOrgAssessmentListScore djOrgAssessmentListScore);

    /**
     * 删除考核评价评分
     *
     * @param id 考核评价评分ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentListScoreById(Long id);

    /**
     * 批量删除考核评价评分
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentListScoreByIds(Long[] ids);

    public List<DjOrgAssessmentListScore> getScoreItem(String assessmentYearUuid);
}
