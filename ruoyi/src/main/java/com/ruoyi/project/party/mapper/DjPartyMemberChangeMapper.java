package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberChange;

/**
 * 党员变更表Mapper接口
 *
 * @author ruoyi
 * @date 2020-09-16
 */
public interface DjPartyMemberChangeMapper
{
    /**
     * 查询党员变更表
     *
     * @param memberId 党员变更表ID
     * @return 党员变更表
     */
    public DjPartyMemberChange selectDjPartyMemberChangeById(Long memberId);


    public DjPartyMemberChange selectDjPartyMemberChangeByMemberUuid(String memberUuid);
    /**
     * 查询党员变更表列表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 党员变更表集合
     */
    public List<DjPartyMemberChange> selectDjPartyMemberChangeList(DjPartyMemberChange djPartyMemberChange);

    /**
     * 新增党员变更表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 结果
     */
    public int insertDjPartyMemberChange(DjPartyMemberChange djPartyMemberChange);

    /**
     * 修改党员变更表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 结果
     */
    public int updateDjPartyMemberChange(DjPartyMemberChange djPartyMemberChange);

    /**
     * 删除党员变更表
     *
     * @param memberId 党员变更表ID
     * @return 结果
     */
    public int deleteDjPartyMemberChangeById(Long memberId);

    /**
     * 批量删除党员变更表
     *
     * @param memberIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberChangeByIds(Long[] memberIds);
}
