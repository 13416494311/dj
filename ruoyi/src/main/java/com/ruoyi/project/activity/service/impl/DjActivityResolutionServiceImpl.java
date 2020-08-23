package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityResolutionMapper;
import com.ruoyi.project.activity.domain.DjActivityResolution;
import com.ruoyi.project.activity.service.IDjActivityResolutionService;

/**
 * 活动决议Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Service
public class DjActivityResolutionServiceImpl implements IDjActivityResolutionService
{
    @Autowired
    private DjActivityResolutionMapper djActivityResolutionMapper;

    /**
     * 查询活动决议
     *
     * @param resolutionId 活动决议ID
     * @return 活动决议
     */
    @Override
    public DjActivityResolution selectDjActivityResolutionById(Long resolutionId)
    {
        return djActivityResolutionMapper.selectDjActivityResolutionById(resolutionId);
    }

    /**
     * 查询活动决议列表
     *
     * @param djActivityResolution 活动决议
     * @return 活动决议
     */
    @Override
    public List<DjActivityResolution> selectDjActivityResolutionList(DjActivityResolution djActivityResolution)
    {
        return djActivityResolutionMapper.selectDjActivityResolutionList(djActivityResolution);
    }

    /**
     * 新增活动决议
     *
     * @param djActivityResolution 活动决议
     * @return 结果
     */
    @Override
    public int insertDjActivityResolution(DjActivityResolution djActivityResolution)
    {
        djActivityResolution.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityResolution.setCreateTime(DateUtils.getNowDate());
        return djActivityResolutionMapper.insertDjActivityResolution(djActivityResolution);
    }

    /**
     * 修改活动决议
     *
     * @param djActivityResolution 活动决议
     * @return 结果
     */
    @Override
    public int updateDjActivityResolution(DjActivityResolution djActivityResolution)
    {
        djActivityResolution.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityResolution.setUpdateTime(DateUtils.getNowDate());
        return djActivityResolutionMapper.updateDjActivityResolution(djActivityResolution);
    }

    /**
     * 批量删除活动决议
     *
     * @param resolutionIds 需要删除的活动决议ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityResolutionByIds(Long[] resolutionIds)
    {
        return djActivityResolutionMapper.deleteDjActivityResolutionByIds(resolutionIds);
    }

    /**
     * 删除活动决议信息
     *
     * @param resolutionId 活动决议ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityResolutionById(Long resolutionId)
    {
        return djActivityResolutionMapper.deleteDjActivityResolutionById(resolutionId);
    }
}
