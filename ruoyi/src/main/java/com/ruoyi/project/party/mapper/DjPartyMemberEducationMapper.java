package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberEducation;

/**
 * 教育经历Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-07
 */
public interface DjPartyMemberEducationMapper 
{
    /**
     * 查询教育经历
     * 
     * @param educationId 教育经历ID
     * @return 教育经历
     */
    public DjPartyMemberEducation selectDjPartyMemberEducationById(Long educationId);

    /**
     * 查询教育经历列表
     * 
     * @param djPartyMemberEducation 教育经历
     * @return 教育经历集合
     */
    public List<DjPartyMemberEducation> selectDjPartyMemberEducationList(DjPartyMemberEducation djPartyMemberEducation);

    /**
     * 新增教育经历
     * 
     * @param djPartyMemberEducation 教育经历
     * @return 结果
     */
    public int insertDjPartyMemberEducation(DjPartyMemberEducation djPartyMemberEducation);

    /**
     * 修改教育经历
     * 
     * @param djPartyMemberEducation 教育经历
     * @return 结果
     */
    public int updateDjPartyMemberEducation(DjPartyMemberEducation djPartyMemberEducation);

    /**
     * 删除教育经历
     * 
     * @param educationId 教育经历ID
     * @return 结果
     */
    public int deleteDjPartyMemberEducationById(Long educationId);

    /**
     * 批量删除教育经历
     * 
     * @param educationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberEducationByIds(Long[] educationIds);
}
