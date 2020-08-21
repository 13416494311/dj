package com.ruoyi.project.activity.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.domain.DjActivityParams;
import com.ruoyi.project.activity.mapper.DjActivityPlanMapper;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.activity.service.IDjActivityPlanService;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityArrangeMapper;
import com.ruoyi.project.activity.domain.DjActivityArrange;
import com.ruoyi.project.activity.service.IDjActivityArrangeService;

/**
 * 活动安排Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-15
 */
@Service
public class DjActivityArrangeServiceImpl implements IDjActivityArrangeService
{
    @Autowired
    private DjActivityArrangeMapper djActivityArrangeMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    /**
     * 查询活动安排
     *
     * @param id 活动安排ID
     * @return 活动安排
     */
    @Override
    public DjActivityArrange selectDjActivityArrangeById(Long id)
    {
        DjActivityArrange arrange = djActivityArrangeMapper.selectDjActivityArrangeById(id) ;
        if(StringUtils.isNotNull(arrange.getPartyMemberId())){
            arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
        }
        if(StringUtils.isNotNull(arrange.getPartyOrgId())){
            arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(arrange.getPlanUuid())){
            arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
        }
        return arrange;
    }

    /**
     * 查询活动安排列表
     *
     * @param djActivityArrange 活动安排
     * @return 活动安排
     */
    @Override
    public List<DjActivityArrange> selectDjActivityArrangeList(DjActivityArrange djActivityArrange)
    {
        List<DjActivityArrange> list = djActivityArrangeMapper.selectDjActivityArrangeList(djActivityArrange);
        list.stream().forEach(arrange->{
            if(StringUtils.isNotNull(arrange.getPartyMemberId())){
                arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(arrange.getPartyOrgId())){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(arrange.getPlanUuid())){
                arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 查询活动安排列表
     *
     * @param map 活动安排
     * @return 活动安排
     */
    @Override
    public List<DjActivityArrange> selectArrangeListByParam(DjActivityParams params)
    {
        List<DjActivityArrange> list = djActivityArrangeMapper.selectArrangeListByParam(params);
        list.stream().forEach(arrange->{
            if(StringUtils.isNotNull(arrange.getPartyMemberId())){
                arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(arrange.getPartyOrgId())){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(arrange.getPlanUuid())){
                arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 新增活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    @Override
    public int insertDjActivityArrange(DjActivityArrange djActivityArrange)
    {
        djActivityArrange.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djActivityArrange.setCreateTime(DateUtils.getNowDate());
        return djActivityArrangeMapper.insertDjActivityArrange(djActivityArrange);
    }

    /**
     * 修改活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    @Override
    public int updateDjActivityArrange(DjActivityArrange djActivityArrange)
    {


        if("2".equals(djActivityArrange.getStatus())){
            DjActivityDetailed activityDetailed =new DjActivityDetailed();
            activityDetailed.setPartyOrgId(djActivityArrange.getPartyOrgId());
            activityDetailed.setPlanUuid(djActivityArrange.getPlanUuid());
            activityDetailed.setPartyMemberId(djActivityArrange.getPartyMemberId());
            activityDetailed.setVenue(djActivityArrange.getVenue());
            activityDetailed.setStatus("0");
            djActivityDetailedService.updateByPlanUuidAndPartyOrgId(activityDetailed);
        }
        djActivityArrange.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityArrange.setUpdateTime(DateUtils.getNowDate());
        return djActivityArrangeMapper.updateDjActivityArrange(djActivityArrange);
    }

    /**
     * 批量删除活动安排
     *
     * @param ids 需要删除的活动安排ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityArrangeByIds(Long[] ids)
    {
        return djActivityArrangeMapper.deleteDjActivityArrangeByIds(ids);
    }

    /**
     * 删除活动安排信息
     *
     * @param id 活动安排ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityArrangeById(Long id)
    {
        return djActivityArrangeMapper.deleteDjActivityArrangeById(id);
    }
}
