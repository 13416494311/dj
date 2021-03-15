package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessment;

/**
 * 党组织考核Service接口
 * 
 * @author admin
 * @date 2021-03-10
 */
public interface IDjOrgAssessmentService 
{
    /**
     * 查询党组织考核
     * 
     * @param id 党组织考核ID
     * @return 党组织考核
     */
    public DjOrgAssessment selectDjOrgAssessmentById(Long id);

    /**
     * 查询党组织考核列表
     * 
     * @param djOrgAssessment 党组织考核
     * @return 党组织考核集合
     */
    public List<DjOrgAssessment> selectDjOrgAssessmentList(DjOrgAssessment djOrgAssessment);

    /**
     * 新增党组织考核
     * 
     * @param djOrgAssessment 党组织考核
     * @return 结果
     */
    public int insertDjOrgAssessment(DjOrgAssessment djOrgAssessment);

    /**
     * 修改党组织考核
     * 
     * @param djOrgAssessment 党组织考核
     * @return 结果
     */
    public int updateDjOrgAssessment(DjOrgAssessment djOrgAssessment);

    /**
     * 批量删除党组织考核
     * 
     * @param ids 需要删除的党组织考核ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentByIds(Long[] ids);

    /**
     * 删除党组织考核信息
     * 
     * @param id 党组织考核ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentById(Long id);
}
