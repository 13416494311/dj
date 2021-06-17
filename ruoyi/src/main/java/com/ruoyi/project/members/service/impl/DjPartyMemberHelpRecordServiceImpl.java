package com.ruoyi.project.members.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.members.mapper.DjPartyMemberHelpRecordMapper;
import com.ruoyi.project.members.domain.DjPartyMemberHelpRecord;
import com.ruoyi.project.members.service.IDjPartyMemberHelpRecordService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 帮扶记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyMemberHelpRecordServiceImpl implements IDjPartyMemberHelpRecordService
{
    @Autowired
    private DjPartyMemberHelpRecordMapper djPartyMemberHelpRecordMapper;

    /**
     * 查询帮扶记录
     *
     * @param helpRecordId 帮扶记录ID
     * @return 帮扶记录
     */
    @Override
    public DjPartyMemberHelpRecord selectDjPartyMemberHelpRecordById(Long helpRecordId)
    {
        return djPartyMemberHelpRecordMapper.selectDjPartyMemberHelpRecordById(helpRecordId);
    }

    /**
     * 查询帮扶记录列表
     *
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 帮扶记录
     */
    @Override
    public List<DjPartyMemberHelpRecord> selectDjPartyMemberHelpRecordList(DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        return djPartyMemberHelpRecordMapper.selectDjPartyMemberHelpRecordList(djPartyMemberHelpRecord);
    }

    /**
     * 新增帮扶记录
     *
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberHelpRecord(DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        djPartyMemberHelpRecord.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberHelpRecordMapper.insertDjPartyMemberHelpRecord(djPartyMemberHelpRecord);
    }

    /**
     * 修改帮扶记录
     *
     * @param djPartyMemberHelpRecord 帮扶记录
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberHelpRecord(DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        djPartyMemberHelpRecord.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberHelpRecordMapper.updateDjPartyMemberHelpRecord(djPartyMemberHelpRecord);
    }

    /**
     * 批量删除帮扶记录
     *
     * @param helpRecordIds 需要删除的帮扶记录ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberHelpRecordByIds(Long[] helpRecordIds)
    {
        return djPartyMemberHelpRecordMapper.deleteDjPartyMemberHelpRecordByIds(helpRecordIds);
    }

    /**
     * 删除帮扶记录信息
     *
     * @param helpRecordId 帮扶记录ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberHelpRecordById(Long helpRecordId)
    {
        return djPartyMemberHelpRecordMapper.deleteDjPartyMemberHelpRecordById(helpRecordId);
    }
}
