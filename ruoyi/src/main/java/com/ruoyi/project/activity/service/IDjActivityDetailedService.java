package com.ruoyi.project.activity.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.domain.DjActivityParams;

/**
 * 活动详情Service接口
 *
 * @author ruoyi
 * @date 2020-08-16
 */
public interface IDjActivityDetailedService
{
    /**
     * 查询活动详情
     *
     * @param activityId 活动详情ID
     * @return 活动详情
     */
    public DjActivityDetailed selectDjActivityDetailedById(Long activityId);

    /**
     * 查询活动详情
     *
     * @param detailedUuid 活动详情uuID
     * @return 活动详情
     */
    public DjActivityDetailed selectDjActivityDetailedByDetailedUuid(String detailedUuid);

    /**
     * 查询活动详情列表
     *
     * @param djActivityDetailed 活动详情
     * @return 活动详情集合
     */
    public List<DjActivityDetailed> selectDjActivityDetailedList(DjActivityDetailed djActivityDetailed);

    /**
     * 查询活动详情列表
     *
     * @param map 活动详情
     * @return 活动详情集合
     */
    public List<DjActivityDetailed> selectDetailedListByParam(DjActivityParams params);

    /**
     * 新增活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    public int insertDjActivityDetailed(DjActivityDetailed djActivityDetailed);

    /**
     * 修改活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    public int updateDjActivityDetailed(DjActivityDetailed djActivityDetailed);

    public int updateByPlanUuidAndPartyOrgId(DjActivityDetailed djActivityDetailed);

    /**
     * 批量删除活动详情
     *
     * @param activityIds 需要删除的活动详情ID
     * @return 结果
     */
    public int deleteDjActivityDetailedByIds(Long[] activityIds);

    /**
     * 删除活动详情信息
     *
     * @param activityId 活动详情ID
     * @return 结果
     */
    public int deleteDjActivityDetailedById(Long activityId);
}
