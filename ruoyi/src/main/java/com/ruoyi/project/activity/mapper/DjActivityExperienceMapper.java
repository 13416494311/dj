package com.ruoyi.project.activity.mapper;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivityExperience;

/**
 * 心得体会Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-23
 */
public interface DjActivityExperienceMapper 
{
    /**
     * 查询心得体会
     * 
     * @param experienceId 心得体会ID
     * @return 心得体会
     */
    public DjActivityExperience selectDjActivityExperienceById(Long experienceId);

    /**
     * 查询心得体会列表
     * 
     * @param djActivityExperience 心得体会
     * @return 心得体会集合
     */
    public List<DjActivityExperience> selectDjActivityExperienceList(DjActivityExperience djActivityExperience);

    /**
     * 新增心得体会
     * 
     * @param djActivityExperience 心得体会
     * @return 结果
     */
    public int insertDjActivityExperience(DjActivityExperience djActivityExperience);

    /**
     * 修改心得体会
     * 
     * @param djActivityExperience 心得体会
     * @return 结果
     */
    public int updateDjActivityExperience(DjActivityExperience djActivityExperience);

    /**
     * 删除心得体会
     * 
     * @param experienceId 心得体会ID
     * @return 结果
     */
    public int deleteDjActivityExperienceById(Long experienceId);

    /**
     * 批量删除心得体会
     * 
     * @param experienceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjActivityExperienceByIds(Long[] experienceIds);
}
