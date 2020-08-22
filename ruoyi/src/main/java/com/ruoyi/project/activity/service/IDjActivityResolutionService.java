package com.ruoyi.project.activity.service;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivityResolution;

/**
 * 活动决议Service接口
 * 
 * @author ruoyi
 * @date 2020-08-22
 */
public interface IDjActivityResolutionService 
{
    /**
     * 查询活动决议
     * 
     * @param resolutionId 活动决议ID
     * @return 活动决议
     */
    public DjActivityResolution selectDjActivityResolutionById(Long resolutionId);

    /**
     * 查询活动决议列表
     * 
     * @param djActivityResolution 活动决议
     * @return 活动决议集合
     */
    public List<DjActivityResolution> selectDjActivityResolutionList(DjActivityResolution djActivityResolution);

    /**
     * 新增活动决议
     * 
     * @param djActivityResolution 活动决议
     * @return 结果
     */
    public int insertDjActivityResolution(DjActivityResolution djActivityResolution);

    /**
     * 修改活动决议
     * 
     * @param djActivityResolution 活动决议
     * @return 结果
     */
    public int updateDjActivityResolution(DjActivityResolution djActivityResolution);

    /**
     * 批量删除活动决议
     * 
     * @param resolutionIds 需要删除的活动决议ID
     * @return 结果
     */
    public int deleteDjActivityResolutionByIds(Long[] resolutionIds);

    /**
     * 删除活动决议信息
     * 
     * @param resolutionId 活动决议ID
     * @return 结果
     */
    public int deleteDjActivityResolutionById(Long resolutionId);
}
