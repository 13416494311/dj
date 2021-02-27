package com.ruoyi.project.members.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.members.mapper.DjPartyMemberHelpMapper;
import com.ruoyi.project.members.domain.DjPartyMemberHelp;
import com.ruoyi.project.members.service.IDjPartyMemberHelpService;

/**
 * 党员帮扶Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Service
public class DjPartyMemberHelpServiceImpl implements IDjPartyMemberHelpService
{
    @Autowired
    private DjPartyMemberHelpMapper djPartyMemberHelpMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    /**
     * 查询党员帮扶
     *
     * @param helpId 党员帮扶ID
     * @return 党员帮扶
     */
    @Override
    public DjPartyMemberHelp selectDjPartyMemberHelpById(Long helpId)
    {
        DjPartyMemberHelp memberHelp = djPartyMemberHelpMapper.selectDjPartyMemberHelpById(helpId);
        if(memberHelp.getPartyOrgId()!=null){
            memberHelp.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(memberHelp.getPartyOrgId()));
        }
        if(memberHelp.getPartyMemberId()!=null){
            DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberHelp.getPartyMemberId());
            memberHelp.setDjPartyMember(partyMember);
        }

        return memberHelp;
    }

    /**
     * 查询党员帮扶列表
     *
     * @param djPartyMemberHelp 党员帮扶
     * @return 党员帮扶
     */
    @Override
    public List<DjPartyMemberHelp> selectDjPartyMemberHelpList(DjPartyMemberHelp djPartyMemberHelp)
    {
        List<DjPartyMemberHelp> memberHelpList = djPartyMemberHelpMapper.selectDjPartyMemberHelpList(djPartyMemberHelp);
        memberHelpList.stream().forEach(memberHelp->{
            if(memberHelp.getPartyOrgId()!=null){
                memberHelp.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(memberHelp.getPartyOrgId()));
            }
            if(memberHelp.getPartyMemberId()!=null){
                DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberHelp.getPartyMemberId());
                memberHelp.setDjPartyMember(partyMember);
            }
        });

        return memberHelpList;
    }

    /**
     * 新增党员帮扶
     *
     * @param djPartyMemberHelp 党员帮扶
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberHelp(DjPartyMemberHelp djPartyMemberHelp)
    {
        djPartyMemberHelp.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberHelpMapper.insertDjPartyMemberHelp(djPartyMemberHelp);
    }

    /**
     * 修改党员帮扶
     *
     * @param djPartyMemberHelp 党员帮扶
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberHelp(DjPartyMemberHelp djPartyMemberHelp)
    {
        djPartyMemberHelp.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberHelpMapper.updateDjPartyMemberHelp(djPartyMemberHelp);
    }

    /**
     * 批量删除党员帮扶
     *
     * @param helpIds 需要删除的党员帮扶ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberHelpByIds(Long[] helpIds)
    {
        return djPartyMemberHelpMapper.deleteDjPartyMemberHelpByIds(helpIds);
    }

    /**
     * 删除党员帮扶信息
     *
     * @param helpId 党员帮扶ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberHelpById(Long helpId)
    {
        return djPartyMemberHelpMapper.deleteDjPartyMemberHelpById(helpId);
    }
}
