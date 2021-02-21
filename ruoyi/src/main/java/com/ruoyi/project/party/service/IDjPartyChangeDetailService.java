package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyChangeDetail;

/**
 * 换届职位详情Service接口
 * 
 * @author ruoyi
 * @date 2021-02-18
 */
public interface IDjPartyChangeDetailService 
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
     * 批量删除换届职位详情
     * 
     * @param changeDetailIds 需要删除的换届职位详情ID
     * @return 结果
     */
    public int deleteDjPartyChangeDetailByIds(Long[] changeDetailIds);

    /**
     * 删除换届职位详情信息
     * 
     * @param changeDetailId 换届职位详情ID
     * @return 结果
     */
    public int deleteDjPartyChangeDetailById(Long changeDetailId);
}
