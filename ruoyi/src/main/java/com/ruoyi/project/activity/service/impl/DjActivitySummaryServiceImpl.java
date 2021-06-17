package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivitySummaryMapper;
import com.ruoyi.project.activity.domain.DjActivitySummary;
import com.ruoyi.project.activity.service.IDjActivitySummaryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 活动纪要Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjActivitySummaryServiceImpl implements IDjActivitySummaryService
{
    @Autowired
    private DjActivitySummaryMapper djActivitySummaryMapper;

    /**
     * 查询活动纪要
     *
     * @param summaryId 活动纪要ID
     * @return 活动纪要
     */
    @Override
    public DjActivitySummary selectDjActivitySummaryById(Long summaryId)
    {
        return djActivitySummaryMapper.selectDjActivitySummaryById(summaryId);
    }

    /**
     * 查询活动纪要列表
     *
     * @param djActivitySummary 活动纪要
     * @return 活动纪要
     */
    @Override
    public List<DjActivitySummary> selectDjActivitySummaryList(DjActivitySummary djActivitySummary)
    {
        return djActivitySummaryMapper.selectDjActivitySummaryList(djActivitySummary);
    }

    /**
     * 新增活动纪要
     *
     * @param djActivitySummary 活动纪要
     * @return 结果
     */
    @Override
    public int insertDjActivitySummary(DjActivitySummary djActivitySummary)
    {
        djActivitySummary.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivitySummary.setCreateTime(DateUtils.getNowDate());
        return djActivitySummaryMapper.insertDjActivitySummary(djActivitySummary);
    }

    /**
     * 修改活动纪要
     *
     * @param djActivitySummary 活动纪要
     * @return 结果
     */
    @Override
    public int updateDjActivitySummary(DjActivitySummary djActivitySummary)
    {
        djActivitySummary.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivitySummary.setUpdateTime(DateUtils.getNowDate());
        return djActivitySummaryMapper.updateDjActivitySummary(djActivitySummary);
    }

    /**
     * 批量删除活动纪要
     *
     * @param summaryIds 需要删除的活动纪要ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySummaryByIds(Long[] summaryIds)
    {
        return djActivitySummaryMapper.deleteDjActivitySummaryByIds(summaryIds);
    }

    /**
     * 删除活动纪要信息
     *
     * @param summaryId 活动纪要ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySummaryById(Long summaryId)
    {
        return djActivitySummaryMapper.deleteDjActivitySummaryById(summaryId);
    }
}
