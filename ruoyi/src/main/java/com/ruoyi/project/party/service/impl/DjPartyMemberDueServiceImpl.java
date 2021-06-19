package com.ruoyi.project.party.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDue;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberDuePlanMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberDueService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 党员党费Service业务层处理
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyMemberDueServiceImpl implements IDjPartyMemberDueService
{
    @Autowired
    private DjPartyMemberDueMapper djPartyMemberDueMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private DjPartyMemberDueOrgMapper djPartyMemberDueOrgMapper;
    @Autowired
    private DjPartyMemberDuePlanMapper djPartyMemberDuePlanMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;

    @Override
    public List<DjPartyMember> selectDueMemberList(DjPartyMemberDue djPartyMemberDue){
        DjPartyMemberDueOrg dueOrg = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgById(djPartyMemberDue.getDueOrgId());

        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setPartyOrgId(dueOrg.getPartyOrgId());
        List<DjPartyMember> djPartyMemberList =djPartyMemberMapper.selectPartyMemberList(djPartyMember);

        djPartyMemberList.stream().forEach(partyMember->{
            djPartyMemberDue.setPartyMemberId(partyMember.getMemberId());
            List<DjPartyMemberDue> partyMemberDueList =  djPartyMemberDueMapper.selectDjPartyMemberDueList(djPartyMemberDue);
            partyMember.setMemberDueList(partyMemberDueList);
        });
        return djPartyMemberList;
    }
    /**
     * 查询党员党费
     *
     * @param dueMemberId 党员党费ID
     * @return 党员党费
     */
    @Override
    public DjPartyMemberDue selectDjPartyMemberDueById(Long dueMemberId)
    {
        return djPartyMemberDueMapper.selectDjPartyMemberDueById(dueMemberId);
    }

    /**
     * 查询党员党费列表
     *
     * @param djPartyMemberDue 党员党费
     * @return 党员党费
     */
    @Override
    public List<DjPartyMemberDue> selectDjPartyMemberDueList(DjPartyMemberDue djPartyMemberDue)
    {
        List<DjPartyMemberDue> memberDueList = djPartyMemberDueMapper.selectDjPartyMemberDueList(djPartyMemberDue);

        memberDueList.stream().forEach(partyMemberDue ->{
            DjPartyMemberDueOrg partyMemberDueOrg = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgById(partyMemberDue.getDueOrgId());
            partyMemberDue.setDuePlan(djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanByUuid(partyMemberDueOrg.getDuePlanUuid()));
            partyMemberDue.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMemberDueOrg.getPartyOrgId()));
            partyMemberDue.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(partyMemberDue.getPartyMemberId()));
        } );
        return memberDueList;
    }

    /**
     * 新增党员党费
     *
     * @param djPartyMemberDue 党员党费
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberDue(DjPartyMemberDue djPartyMemberDue)
    {
        djPartyMemberDue.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDue.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberDueMapper.insertDjPartyMemberDue(djPartyMemberDue);
    }

    /**
     * 修改党员党费
     *
     * @param djPartyMemberDue 党员党费
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberDue(DjPartyMemberDue djPartyMemberDue)
    {
        djPartyMemberDue.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDue.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberDueMapper.updateDjPartyMemberDue(djPartyMemberDue);
    }

    /**
     * 批量删除党员党费
     *
     * @param dueMemberIds 需要删除的党员党费ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDueByIds(Long[] dueMemberIds)
    {
        return djPartyMemberDueMapper.deleteDjPartyMemberDueByIds(dueMemberIds);
    }

    /**
     * 删除党员党费信息
     *
     * @param dueMemberId 党员党费ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDueById(Long dueMemberId)
    {
        return djPartyMemberDueMapper.deleteDjPartyMemberDueById(dueMemberId);
    }
}
