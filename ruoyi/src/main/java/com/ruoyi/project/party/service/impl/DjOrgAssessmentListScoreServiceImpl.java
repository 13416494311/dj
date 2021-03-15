package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentListScoreMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentListScore;
import com.ruoyi.project.party.service.IDjOrgAssessmentListScoreService;

/**
 * 考核评价评分Service业务层处理
 * 
 * @author admin
 * @date 2021-03-12
 */
@Service
public class DjOrgAssessmentListScoreServiceImpl implements IDjOrgAssessmentListScoreService 
{
    @Autowired
    private DjOrgAssessmentListScoreMapper djOrgAssessmentListScoreMapper;

    /**
     * 查询考核评价评分
     * 
     * @param id 考核评价评分ID
     * @return 考核评价评分
     */
    @Override
    public DjOrgAssessmentListScore selectDjOrgAssessmentListScoreById(Long id)
    {
        return djOrgAssessmentListScoreMapper.selectDjOrgAssessmentListScoreById(id);
    }

    /**
     * 查询考核评价评分列表
     * 
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 考核评价评分
     */
    @Override
    public List<DjOrgAssessmentListScore> selectDjOrgAssessmentListScoreList(DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        return djOrgAssessmentListScoreMapper.selectDjOrgAssessmentListScoreList(djOrgAssessmentListScore);
    }

    /**
     * 新增考核评价评分
     * 
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentListScore(DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        djOrgAssessmentListScore.setDelFlag("0");
        djOrgAssessmentListScore.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentListScore.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentListScoreMapper.insertDjOrgAssessmentListScore(djOrgAssessmentListScore);
    }

    /**
     * 修改考核评价评分
     * 
     * @param djOrgAssessmentListScore 考核评价评分
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentListScore(DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        djOrgAssessmentListScore.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentListScore.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentListScoreMapper.updateDjOrgAssessmentListScore(djOrgAssessmentListScore);
    }

    /**
     * 批量删除考核评价评分
     * 
     * @param ids 需要删除的考核评价评分ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentListScoreByIds(Long[] ids)
    {
        return djOrgAssessmentListScoreMapper.deleteDjOrgAssessmentListScoreByIds(ids);
    }

    /**
     * 删除考核评价评分信息
     * 
     * @param id 考核评价评分ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentListScoreById(Long id)
    {
        return djOrgAssessmentListScoreMapper.deleteDjOrgAssessmentListScoreById(id);
    }
}
