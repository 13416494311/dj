package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDue;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberDueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberDuePlanMapper;
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;
import com.ruoyi.project.party.service.IDjPartyMemberDuePlanService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 党费计划Service业务层处理
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyMemberDuePlanServiceImpl implements IDjPartyMemberDuePlanService
{
    @Autowired
    private DjPartyMemberDuePlanMapper djPartyMemberDuePlanMapper;
    @Autowired
    private DjPartyMemberDueOrgMapper djPartyMemberDueOrgMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyMemberDueService djPartyMemberDueService;

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
        djPartyMemberDuePlan.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDuePlan.setCreateTime(DateUtils.getNowDate());
        int result = djPartyMemberDuePlanMapper.insertDjPartyMemberDuePlan(djPartyMemberDuePlan);
        addMemberDue(djPartyMemberDuePlan);
        return result;
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
        djPartyMemberDuePlan.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDuePlan.setUpdateTime(DateUtils.getNowDate());
        int result = djPartyMemberDuePlanMapper.updateDjPartyMemberDuePlan(djPartyMemberDuePlan);
        addMemberDue(djPartyMemberDuePlan);
        return result;
    }

    private void addMemberDue(DjPartyMemberDuePlan djPartyMemberDuePlan){
        if("2".equals(djPartyMemberDuePlan.getStatus())){
            DjPartyMemberDueOrg djPartyMemberDueOrg = new DjPartyMemberDueOrg();
            djPartyMemberDueOrg.setDuePlanUuid(djPartyMemberDuePlan.getDuePlanUuid());
            List<DjPartyMemberDueOrg> dueOrgList = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);
            dueOrgList.stream().forEach(dueOrg->{
                DjPartyMember djPartyMember = new DjPartyMember();
                djPartyMember.setPartyOrgId(dueOrg.getPartyOrgId());
                List<DjPartyMember> djPartyMemberList =djPartyMemberMapper.selectPartyMemberList(djPartyMember);
                djPartyMemberList.stream().forEach(partyMember -> {
                    for(int i=1 ;i<4;i++){
                        DjPartyMemberDue memberDue = new DjPartyMemberDue();
                        memberDue.setDueOrgId(dueOrg.getDueOrgId());
                        memberDue.setPartyMemberId(partyMember.getMemberId());
                        memberDue.setMonth(   (djPartyMemberDuePlan.getQuarter()-1)*3+i    );
                        djPartyMemberDueService.insertDjPartyMemberDue(memberDue);
                    }
                });
            });
        }
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
