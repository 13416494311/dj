package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.mapper.DjOrgAssessmentPerformanceScoreMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentPerformanceScore;
import com.ruoyi.project.party.service.IDjOrgAssessmentPerformanceScoreService;

/**
 * 绩效考核评分Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-20
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentPerformanceScoreServiceImpl implements IDjOrgAssessmentPerformanceScoreService
{
    @Autowired
    private DjOrgAssessmentPerformanceScoreMapper djOrgAssessmentPerformanceScoreMapper;

    /**
     * 查询绩效考核评分
     *
     * @param id 绩效考核评分ID
     * @return 绩效考核评分
     */
    @Override
    public DjOrgAssessmentPerformanceScore selectDjOrgAssessmentPerformanceScoreById(Long id)
    {
        return djOrgAssessmentPerformanceScoreMapper.selectDjOrgAssessmentPerformanceScoreById(id);
    }

    /**
     * 查询绩效考核评分列表
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 绩效考核评分
     */
    @Override
    public List<DjOrgAssessmentPerformanceScore> selectDjOrgAssessmentPerformanceScoreList(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        return djOrgAssessmentPerformanceScoreMapper.selectDjOrgAssessmentPerformanceScoreList(djOrgAssessmentPerformanceScore);
    }

    /**
     * 新增绩效考核评分
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentPerformanceScore(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        djOrgAssessmentPerformanceScore.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djOrgAssessmentPerformanceScore.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentPerformanceScoreMapper.insertDjOrgAssessmentPerformanceScore(djOrgAssessmentPerformanceScore);
    }

    /**
     * 修改绩效考核评分
     *
     * @param djOrgAssessmentPerformanceScore 绩效考核评分
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentPerformanceScore(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        djOrgAssessmentPerformanceScore.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentPerformanceScore.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentPerformanceScoreMapper.updateDjOrgAssessmentPerformanceScore(djOrgAssessmentPerformanceScore);
    }

    /**
     * 批量删除绩效考核评分
     *
     * @param ids 需要删除的绩效考核评分ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentPerformanceScoreByIds(Long[] ids)
    {
        return djOrgAssessmentPerformanceScoreMapper.deleteDjOrgAssessmentPerformanceScoreByIds(ids);
    }

    /**
     * 删除绩效考核评分信息
     *
     * @param id 绩效考核评分ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentPerformanceScoreById(Long id)
    {
        return djOrgAssessmentPerformanceScoreMapper.deleteDjOrgAssessmentPerformanceScoreById(id);
    }

    public List<String> getScoreItem(String assessmentYearUuid){
        return djOrgAssessmentPerformanceScoreMapper.getScoreItem(assessmentYearUuid);
    }
}
