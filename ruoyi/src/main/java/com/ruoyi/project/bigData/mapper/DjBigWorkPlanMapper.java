package com.ruoyi.project.bigData.mapper;

import java.util.List;
import com.ruoyi.project.bigData.domain.DjBigWorkPlan;

/**
 * 党建工作计划Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-31
 */
public interface DjBigWorkPlanMapper 
{
    /**
     * 查询党建工作计划
     * 
     * @param id 党建工作计划ID
     * @return 党建工作计划
     */
    public DjBigWorkPlan selectDjBigWorkPlanById(Long id);

    /**
     * 查询党建工作计划列表
     * 
     * @param djBigWorkPlan 党建工作计划
     * @return 党建工作计划集合
     */
    public List<DjBigWorkPlan> selectDjBigWorkPlanList(DjBigWorkPlan djBigWorkPlan);

    /**
     * 新增党建工作计划
     * 
     * @param djBigWorkPlan 党建工作计划
     * @return 结果
     */
    public int insertDjBigWorkPlan(DjBigWorkPlan djBigWorkPlan);

    /**
     * 修改党建工作计划
     * 
     * @param djBigWorkPlan 党建工作计划
     * @return 结果
     */
    public int updateDjBigWorkPlan(DjBigWorkPlan djBigWorkPlan);

    /**
     * 删除党建工作计划
     * 
     * @param id 党建工作计划ID
     * @return 结果
     */
    public int deleteDjBigWorkPlanById(Long id);

    /**
     * 批量删除党建工作计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjBigWorkPlanByIds(Long[] ids);
}
