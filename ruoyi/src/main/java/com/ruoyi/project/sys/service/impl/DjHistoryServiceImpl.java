package com.ruoyi.project.sys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.sys.mapper.DjHistoryMapper;
import com.ruoyi.project.sys.domain.DjHistory;
import com.ruoyi.project.sys.service.IDjHistoryService;

/**
 * 党史Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-27
 */
@Service
public class DjHistoryServiceImpl implements IDjHistoryService 
{
    @Autowired
    private DjHistoryMapper djHistoryMapper;

    /**
     * 查询党史
     * 
     * @param historyId 党史ID
     * @return 党史
     */
    @Override
    public DjHistory selectDjHistoryById(Long historyId)
    {
        return djHistoryMapper.selectDjHistoryById(historyId);
    }

    /**
     * 查询党史列表
     * 
     * @param djHistory 党史
     * @return 党史
     */
    @Override
    public List<DjHistory> selectDjHistoryList(DjHistory djHistory)
    {
        return djHistoryMapper.selectDjHistoryList(djHistory);
    }

    /**
     * 新增党史
     * 
     * @param djHistory 党史
     * @return 结果
     */
    @Override
    public int insertDjHistory(DjHistory djHistory)
    {
        djHistory.setCreateTime(DateUtils.getNowDate());
        return djHistoryMapper.insertDjHistory(djHistory);
    }

    /**
     * 修改党史
     * 
     * @param djHistory 党史
     * @return 结果
     */
    @Override
    public int updateDjHistory(DjHistory djHistory)
    {
        djHistory.setUpdateTime(DateUtils.getNowDate());
        return djHistoryMapper.updateDjHistory(djHistory);
    }

    /**
     * 批量删除党史
     * 
     * @param historyIds 需要删除的党史ID
     * @return 结果
     */
    @Override
    public int deleteDjHistoryByIds(Long[] historyIds)
    {
        return djHistoryMapper.deleteDjHistoryByIds(historyIds);
    }

    /**
     * 删除党史信息
     * 
     * @param historyId 党史ID
     * @return 结果
     */
    @Override
    public int deleteDjHistoryById(Long historyId)
    {
        return djHistoryMapper.deleteDjHistoryById(historyId);
    }
}
