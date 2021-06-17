package com.ruoyi.project.party.service;

import java.util.List;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDue;

/**
 * 党员党费Service接口
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
public interface IDjPartyMemberDueService
{
    public List<DjPartyMember> selectDueMemberList(DjPartyMemberDue djPartyMemberDu);
    /**
     * 查询党员党费
     *
     * @param dueMemberId 党员党费ID
     * @return 党员党费
     */
    public DjPartyMemberDue selectDjPartyMemberDueById(Long dueMemberId);

    /**
     * 查询党员党费列表
     *
     * @param djPartyMemberDue 党员党费
     * @return 党员党费集合
     */
    public List<DjPartyMemberDue> selectDjPartyMemberDueList(DjPartyMemberDue djPartyMemberDue);

    /**
     * 新增党员党费
     *
     * @param djPartyMemberDue 党员党费
     * @return 结果
     */
    public int insertDjPartyMemberDue(DjPartyMemberDue djPartyMemberDue);

    /**
     * 修改党员党费
     *
     * @param djPartyMemberDue 党员党费
     * @return 结果
     */
    public int updateDjPartyMemberDue(DjPartyMemberDue djPartyMemberDue);

    /**
     * 批量删除党员党费
     *
     * @param dueMemberIds 需要删除的党员党费ID
     * @return 结果
     */
    public int deleteDjPartyMemberDueByIds(Long[] dueMemberIds);

    /**
     * 删除党员党费信息
     *
     * @param dueMemberId 党员党费ID
     * @return 结果
     */
    public int deleteDjPartyMemberDueById(Long dueMemberId);
}
