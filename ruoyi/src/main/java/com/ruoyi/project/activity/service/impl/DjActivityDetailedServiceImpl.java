package com.ruoyi.project.activity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.service.IDjActivityPlanService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityDetailedMapper;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;

/**
 * 活动详情Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Service
public class DjActivityDetailedServiceImpl implements IDjActivityDetailedService
{
    @Autowired
    private DjActivityDetailedMapper djActivityDetailedMapper;
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;

    /**
     * 查询活动详情
     *
     * @param activityId 活动详情ID
     * @return 活动详情
     */
    @Override
    public DjActivityDetailed selectDjActivityDetailedById(Long activityId)
    {
        DjActivityDetailed detailed = djActivityDetailedMapper.selectDjActivityDetailedById(activityId);
        if(StringUtils.isNotNull(detailed.getPartyMemberId())){
            detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
        }
        if(StringUtils.isNotNull(detailed.getPartyOrgId())){
            detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(detailed.getPlanUuid())){
            detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
        }
        return detailed;
    }

    /**
     * 查询活动详情列表
     *
     * @param djActivityDetailed 活动详情
     * @return 活动详情
     */
    @Override
    public List<DjActivityDetailed> selectDjActivityDetailedList(DjActivityDetailed djActivityDetailed)
    {
        List<DjActivityDetailed> list = djActivityDetailedMapper.selectDjActivityDetailedList(djActivityDetailed);
        list.stream().forEach(detailed->{
            if(StringUtils.isNotNull(detailed.getPartyMemberId())){
                detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(detailed.getPartyOrgId())){
                detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(detailed.getPlanUuid())){
                detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
            }
        });
        return list;
    }

    @Override
    public List<DjActivityDetailed> selectDetailedListByParamMap(Map<String,Object> map)
    {
        List<DjActivityDetailed> list = djActivityDetailedMapper.selectDetailedListByParamMap(map);
        list.stream().forEach(detailed->{
            if(StringUtils.isNotNull(detailed.getPartyMemberId())){
                detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(detailed.getPartyOrgId())){
                detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(detailed.getPlanUuid())){
                detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 新增活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    @Override
    public int insertDjActivityDetailed(DjActivityDetailed djActivityDetailed)
    {
        djActivityDetailed.setActivityUuid(UUID.randomUUID().toString());
        djActivityDetailed.setCreateBy(SecurityUtils.getUsername());
        djActivityDetailed.setCreateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.insertDjActivityDetailed(djActivityDetailed);
    }

    /**
     * 修改活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    @Override
    public int updateDjActivityDetailed(DjActivityDetailed djActivityDetailed)
    {
        djActivityDetailed.setUpdateBy(SecurityUtils.getUsername());
        djActivityDetailed.setUpdateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.updateDjActivityDetailed(djActivityDetailed);
    }

    @Override
    public int updateByPlanUuidAndPartyOrgId(DjActivityDetailed djActivityDetailed)
    {

        djActivityDetailed.setUpdateBy(SecurityUtils.getUsername());
        djActivityDetailed.setUpdateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.updateByPlanUuidAndPartyOrgId(djActivityDetailed);
    }

    /**
     * 批量删除活动详情
     *
     * @param activityIds 需要删除的活动详情ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityDetailedByIds(Long[] activityIds)
    {
        return djActivityDetailedMapper.deleteDjActivityDetailedByIds(activityIds);
    }

    /**
     * 删除活动详情信息
     *
     * @param activityId 活动详情ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityDetailedById(Long activityId)
    {
        return djActivityDetailedMapper.deleteDjActivityDetailedById(activityId);
    }
}
