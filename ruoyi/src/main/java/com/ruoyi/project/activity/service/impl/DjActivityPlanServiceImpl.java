package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityPlanMapper;
import com.ruoyi.project.activity.domain.DjActivityPlan;
import com.ruoyi.project.activity.service.IDjActivityPlanService;

/**
 * 活动计划Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-10
 */
@Service
public class DjActivityPlanServiceImpl implements IDjActivityPlanService
{
    @Autowired
    private DjActivityPlanMapper djActivityPlanMapper;

    /**
     * 查询活动计划
     *
     * @param planId 活动计划ID
     * @return 活动计划
     */
    @Override
    public DjActivityPlan selectDjActivityPlanById(Long planId)
    {
        return djActivityPlanMapper.selectDjActivityPlanById(planId);
    }

    /**
     * 查询活动计划列表
     *
     * @param djActivityPlan 活动计划
     * @return 活动计划
     */
    @Override
    public List<DjActivityPlan> selectDjActivityPlanList(DjActivityPlan djActivityPlan)
    {
        return djActivityPlanMapper.selectDjActivityPlanList(djActivityPlan);
    }

    /**
     * 新增活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    @Override
    public int insertDjActivityPlan(DjActivityPlan djActivityPlan)
    {
        djActivityPlan.setCreateTime(DateUtils.getNowDate());
        return djActivityPlanMapper.insertDjActivityPlan(djActivityPlan);
    }

    /**
     * 修改活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    @Override
    public int updateDjActivityPlan(DjActivityPlan djActivityPlan)
    {
        djActivityPlan.setUpdateTime(DateUtils.getNowDate());
        return djActivityPlanMapper.updateDjActivityPlan(djActivityPlan);
    }

    /**
     * 批量删除活动计划
     *
     * @param planIds 需要删除的活动计划ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityPlanByIds(Long[] planIds)
    {
        return djActivityPlanMapper.deleteDjActivityPlanByIds(planIds);
    }

    /**
     * 删除活动计划信息
     *
     * @param planId 活动计划ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityPlanById(Long planId)
    {
        return djActivityPlanMapper.deleteDjActivityPlanById(planId);
    }
}
