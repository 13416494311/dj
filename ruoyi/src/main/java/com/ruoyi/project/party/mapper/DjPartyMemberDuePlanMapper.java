package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;

/**
 * 党费计划Mapper接口
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
public interface DjPartyMemberDuePlanMapper
{
    /**
     * 查询党费计划
     *
     * @param duePlanId 党费计划ID
     * @return 党费计划
     */
    public DjPartyMemberDuePlan selectDjPartyMemberDuePlanById(Long duePlanId);

    /**
     * 查询党费计划
     *
     * @param duePlanUuid 党费计划ID
     * @return 党费计划
     */
    public DjPartyMemberDuePlan selectDjPartyMemberDuePlanByUuid(String duePlanUuid);

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
     * 删除党费计划
     *
     * @param duePlanId 党费计划ID
     * @return 结果
     */
    public int deleteDjPartyMemberDuePlanById(Long duePlanId);

    /**
     * 批量删除党费计划
     *
     * @param duePlanIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberDuePlanByIds(Long[] duePlanIds);
}
