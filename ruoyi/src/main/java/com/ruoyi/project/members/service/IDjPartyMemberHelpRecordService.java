package com.ruoyi.project.members.service;

import java.util.List;
import com.ruoyi.project.members.domain.DjPartyMemberHelpRecord;

/**
 * 帮扶记录Service接口
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
public interface IDjPartyMemberHelpRecordService 
{
    /**
     * 查询帮扶记录
     * 
     * @param helpRecordId 帮扶记录ID
     * @return 帮扶记录
     */
    public DjPartyMemberHelpRecord selectDjPartyMemberHelpRecordById(Long helpRecordId);

    /**
     * 查询帮扶记录列表
     * 
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 帮扶记录集合
     */
    public List<DjPartyMemberHelpRecord> selectDjPartyMemberHelpRecordList(DjPartyMemberHelpRecord djPartyMemberHelpRecord);

    /**
     * 新增帮扶记录
     * 
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 结果
     */
    public int insertDjPartyMemberHelpRecord(DjPartyMemberHelpRecord djPartyMemberHelpRecord);

    /**
     * 修改帮扶记录
     * 
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 结果
     */
    public int updateDjPartyMemberHelpRecord(DjPartyMemberHelpRecord djPartyMemberHelpRecord);

    /**
     * 批量删除帮扶记录
     * 
     * @param helpRecordIds 需要删除的帮扶记录ID
     * @return 结果
     */
    public int deleteDjPartyMemberHelpRecordByIds(Long[] helpRecordIds);

    /**
     * 删除帮扶记录信息
     * 
     * @param helpRecordId 帮扶记录ID
     * @return 结果
     */
    public int deleteDjPartyMemberHelpRecordById(Long helpRecordId);
}
