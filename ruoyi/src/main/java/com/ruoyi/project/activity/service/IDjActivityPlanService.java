package com.ruoyi.project.activity.service;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivityPlan;

/**
 * 活动计划Service接口
 *
 * @author ruoyi
 * @date 2020-08-10
 */
public interface IDjActivityPlanService
{
    /**
     * 查询活动计划
     *
     * @param planId 活动计划ID
     * @return 活动计划
     */
    public DjActivityPlan selectDjActivityPlanById(Long planId);

    public DjActivityPlan selectDjActivityPlanByPlanUuid(String planUuid);
    /**
     * 查询活动计划列表
     *
     * @param djActivityPlan 活动计划
     * @return 活动计划集合
     */
    public List<DjActivityPlan> selectDjActivityPlanList(DjActivityPlan djActivityPlan);

    /**
     * 新增活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    public int insertDjActivityPlan(DjActivityPlan djActivityPlan);

    /**
     * 修改活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    public int updateDjActivityPlan(DjActivityPlan djActivityPlan);

    /**
     * 批量删除活动计划
     *
     * @param planIds 需要删除的活动计划ID
     * @return 结果
     */
    public int deleteDjActivityPlanByIds(Long[] planIds);

    /**
     * 删除活动计划信息
     *
     * @param planId 活动计划ID
     * @return 结果
     */
    public int deleteDjActivityPlanById(Long planId);
}
