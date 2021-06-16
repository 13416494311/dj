package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;

/**
 * 党费计划Service接口
 * 
 * @author wandengfeng
 * @date 2021-06-15
 */
public interface IDjPartyMemberDuePlanService 
{
    /**
     * 查询党费计划
     * 
     * @param duePlanId 党费计划ID
     * @return 党费计划
     */
    public DjPartyMemberDuePlan selectDjPartyMemberDuePlanById(Long duePlanId);

    /**
     * 查询党费计划列表
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 党费计划集合
     */
    public List<DjPartyMemberDuePlan> selectDjPartyMemberDuePlanList(DjPartyMemberDuePlan djPartyMemberDuePlan);

    /**
     * 新增党费计划
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    public int insertDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan);

    /**
     * 修改党费计划
     * 
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    public int updateDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan);

    /**
     * 批量删除党费计划
     * 
     * @param duePlanIds 需要删除的党费计划ID
     * @return 结果
     */
    public int deleteDjPartyMemberDuePlanByIds(Long[] duePlanIds);

    /**
     * 删除党费计划信息
     * 
     * @param duePlanId 党费计划ID
     * @return 结果
     */
    public int deleteDjPartyMemberDuePlanById(Long duePlanId);
}
