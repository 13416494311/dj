package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberDueMapper;
import com.ruoyi.project.party.domain.DjPartyMemberDue;
import com.ruoyi.project.party.service.IDjPartyMemberDueService;

/**
 * 党员党费Service业务层处理
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
@Service
public class DjPartyMemberDueServiceImpl implements IDjPartyMemberDueService
{
    @Autowired
    private DjPartyMemberDueMapper djPartyMemberDueMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private DjPartyMemberDueOrgMapper djPartyMemberDueOrgMapper;

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
        return djPartyMemberDueMapper.selectDjPartyMemberDueList(djPartyMemberDue);
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
