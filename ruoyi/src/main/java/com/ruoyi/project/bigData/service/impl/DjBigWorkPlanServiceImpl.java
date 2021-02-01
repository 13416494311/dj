package com.ruoyi.project.bigData.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.bigData.mapper.DjBigWorkPlanMapper;
import com.ruoyi.project.bigData.domain.DjBigWorkPlan;
import com.ruoyi.project.bigData.service.IDjBigWorkPlanService;

/**
 * 党建工作计划Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-31
 */
@Service
public class DjBigWorkPlanServiceImpl implements IDjBigWorkPlanService
{
    @Autowired
    private DjBigWorkPlanMapper djBigWorkPlanMapper;

    /**
     * 查询党建工作计划
     *
     * @param id 党建工作计划ID
     * @return 党建工作计划
     */
    @Override
    public DjBigWorkPlan selectDjBigWorkPlanById(Long id)
    {
        return djBigWorkPlanMapper.selectDjBigWorkPlanById(id);
    }

    /**
     * 查询党建工作计划列表
     *
     * @param djBigWorkPlan 党建工作计划
     * @return 党建工作计划
     */
    @Override
    public List<DjBigWorkPlan> selectDjBigWorkPlanList(DjBigWorkPlan djBigWorkPlan)
    {
        return djBigWorkPlanMapper.selectDjBigWorkPlanList(djBigWorkPlan);
    }

    /**
     * 新增党建工作计划
     *
     * @param djBigWorkPlan 党建工作计划
     * @return 结果
     */
    @Override
    public int insertDjBigWorkPlan(DjBigWorkPlan djBigWorkPlan)
    {
        djBigWorkPlan.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djBigWorkPlan.setCreateTime(DateUtils.getNowDate());
        return djBigWorkPlanMapper.insertDjBigWorkPlan(djBigWorkPlan);
    }

    /**
     * 修改党建工作计划
     *
     * @param djBigWorkPlan 党建工作计划
     * @return 结果
     */
    @Override
    public int updateDjBigWorkPlan(DjBigWorkPlan djBigWorkPlan)
    {
        djBigWorkPlan.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djBigWorkPlan.setUpdateTime(DateUtils.getNowDate());
        return djBigWorkPlanMapper.updateDjBigWorkPlan(djBigWorkPlan);
    }

    /**
     * 批量删除党建工作计划
     *
     * @param ids 需要删除的党建工作计划ID
     * @return 结果
     */
    @Override
    public int deleteDjBigWorkPlanByIds(Long[] ids)
    {
        return djBigWorkPlanMapper.deleteDjBigWorkPlanByIds(ids);
    }

    /**
     * 删除党建工作计划信息
     *
     * @param id 党建工作计划ID
     * @return 结果
     */
    @Override
    public int deleteDjBigWorkPlanById(Long id)
    {
        return djBigWorkPlanMapper.deleteDjBigWorkPlanById(id);
    }
}
