package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyChangeDetail;

/**
 * 换届职位详情Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-18
 */
public interface DjPartyChangeDetailMapper 
{
    /**
     * 查询换届职位详情
     * 
     * @param changeDetailId 换届职位详情ID
     * @return 换届职位详情
     */
    public DjPartyChangeDetail selectDjPartyChangeDetailById(Long changeDetailId);

    /**
     * 查询换届职位详情列表
     * 
     * @param djPartyChangeDetail 换届职位详情
     * @return 换届职位详情集合
     */
    public List<DjPartyChangeDetail> selectDjPartyChangeDetailList(DjPartyChangeDetail djPartyChangeDetail);

    /**
     * 新增换届职位详情
     * 
     * @param djPartyChangeDetail 换届职位详情
     * @return 结果
     */
    public int insertDjPartyChangeDetail(DjPartyChangeDetail djPartyChangeDetail);

    /**
     * 修改换届职位详情
     * 
     * @param djPartyChangeDetail 换届职位详情
     * @return 结果
     */
    public int updateDjPartyChangeDetail(DjPartyChangeDetail djPartyChangeDetail);

    /**
     * 删除换届职位详情
     * 
     * @param changeDetailId 换届职位详情ID
     * @return 结果
     */
    public int deleteDjPartyChangeDetailById(Long changeDetailId);

    /**
     * 批量删除换届职位详情
     * 
     * @param changeDetailIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyChangeDetailByIds(Long[] changeDetailIds);
}
