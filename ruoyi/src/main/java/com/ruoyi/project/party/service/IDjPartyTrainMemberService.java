package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyTrainMember;

/**
 * 参与培训党员Service接口
 * 
 * @author ruoyi
 * @date 2021-03-09
 */
public interface IDjPartyTrainMemberService 
{
    /**
     * 查询参与培训党员
     * 
     * @param trainMemberId 参与培训党员ID
     * @return 参与培训党员
     */
    public DjPartyTrainMember selectDjPartyTrainMemberById(Long trainMemberId);

    /**
     * 查询参与培训党员列表
     * 
     * @param djPartyTrainMember 参与培训党员
     * @return 参与培训党员集合
     */
    public List<DjPartyTrainMember> selectDjPartyTrainMemberList(DjPartyTrainMember djPartyTrainMember);

    /**
     * 新增参与培训党员
     * 
     * @param djPartyTrainMember 参与培训党员
     * @return 结果
     */
    public int insertDjPartyTrainMember(DjPartyTrainMember djPartyTrainMember);

    /**
     * 修改参与培训党员
     * 
     * @param djPartyTrainMember 参与培训党员
     * @return 结果
     */
    public int updateDjPartyTrainMember(DjPartyTrainMember djPartyTrainMember);

    /**
     * 批量删除参与培训党员
     * 
     * @param trainMemberIds 需要删除的参与培训党员ID
     * @return 结果
     */
    public int deleteDjPartyTrainMemberByIds(Long[] trainMemberIds);

    /**
     * 删除参与培训党员信息
     * 
     * @param trainMemberId 参与培训党员ID
     * @return 结果
     */
    public int deleteDjPartyTrainMemberById(Long trainMemberId);
}
