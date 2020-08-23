package com.ruoyi.project.activity.service;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivitySupervise;

/**
 * 活动督办Service接口
 * 
 * @author ruoyi
 * @date 2020-08-23
 */
public interface IDjActivitySuperviseService 
{
    /**
     * 查询活动督办
     * 
     * @param superviseId 活动督办ID
     * @return 活动督办
     */
    public DjActivitySupervise selectDjActivitySuperviseById(Long superviseId);

    /**
     * 查询活动督办列表
     * 
     * @param djActivitySupervise 活动督办
     * @return 活动督办集合
     */
    public List<DjActivitySupervise> selectDjActivitySuperviseList(DjActivitySupervise djActivitySupervise);

    /**
     * 新增活动督办
     * 
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    public int insertDjActivitySupervise(DjActivitySupervise djActivitySupervise);

    /**
     * 修改活动督办
     * 
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    public int updateDjActivitySupervise(DjActivitySupervise djActivitySupervise);

    /**
     * 批量删除活动督办
     * 
     * @param superviseIds 需要删除的活动督办ID
     * @return 结果
     */
    public int deleteDjActivitySuperviseByIds(Long[] superviseIds);

    /**
     * 删除活动督办信息
     * 
     * @param superviseId 活动督办ID
     * @return 结果
     */
    public int deleteDjActivitySuperviseById(Long superviseId);
}
