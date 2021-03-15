package com.ruoyi.project.party.mapper;

import java.util.List;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyTrainMember;

/**
 * 参与培训党员Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-09
 */
public interface DjPartyTrainMemberMapper
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

    public List<DjPartyTrainMember> getTrainRankInfo(DjPartyMember djPartyMember);

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
     * 删除参与培训党员
     *
     * @param trainMemberId 参与培训党员ID
     * @return 结果
     */
    public int deleteDjPartyTrainMemberById(Long trainMemberId);

    /**
     * 批量删除参与培训党员
     *
     * @param trainMemberIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyTrainMemberByIds(Long[] trainMemberIds);
}
