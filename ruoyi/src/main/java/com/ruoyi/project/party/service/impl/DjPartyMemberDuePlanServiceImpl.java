package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberDuePlanMapper;
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;
import com.ruoyi.project.party.service.IDjPartyMemberDuePlanService;

/**
 * 党费计划Service业务层处理
 * 
 * @author wandengfeng
 * @date 2021-06-15
 */
@Service
public class DjPartyMemberDuePlanServiceImpl implements IDjPartyMemberDuePlanService 
{
    @Autowired
    private DjPartyMemberDuePlanMapper djPartyMemberDuePlanMapper;

    /**
     * 查询党费计划
     * 
     * @param duePlanId 党费计划ID
     * @return 党费计划
     */
    @Override
    public DjPartyMemberDuePlan selectDjPartyMemberDuePlanById(Long duePlanId)
    {
        return djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanById(duePlanId);
    }

    /**
     * 查询党费计划列表
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 党费计划
     */
    @Override
    public List<DjPartyMemberDuePlan> selectDjPartyMemberDuePlanList(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        return djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanList(djPartyMemberDuePlan);
    }

    /**
     * 新增党费计划
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        djPartyMemberDuePlan.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberDuePlanMapper.insertDjPartyMemberDuePlan(djPartyMemberDuePlan);
    }

    /**
     * 修改党费计划
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        djPartyMemberDuePlan.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberDuePlanMapper.updateDjPartyMemberDuePlan(djPartyMemberDuePlan);
    }

    /**
     * 批量删除党费计划
     * 
     * @param duePlanIds 需要删除的党费计划ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDuePlanByIds(Long[] duePlanIds)
    {
        return djPartyMemberDuePlanMapper.deleteDjPartyMemberDuePlanByIds(duePlanIds);
    }

    /**
     * 删除党费计划信息
     * 
     * @param duePlanId 党费计划ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDuePlanById(Long duePlanId)
    {
        return djPartyMemberDuePlanMapper.deleteDjPartyMemberDuePlanById(duePlanId);
    }
}
