package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentAddsubtract;

/**
 * 党组织加减分审核Mapper接口
 * 
 * @author admin
 * @date 2021-03-15
 */
public interface DjOrgAssessmentAddsubtractMapper 
{
    /**
     * 查询党组织加减分审核
     * 
     * @param addsubtractId 党组织加减分审核ID
     * @return 党组织加减分审核
     */
    public DjOrgAssessmentAddsubtract selectDjOrgAssessmentAddsubtractById(Long addsubtractId);

    /**
     * 查询党组织加减分审核列表
     * 
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 党组织加减分审核集合
     */
    public List<DjOrgAssessmentAddsubtract> selectDjOrgAssessmentAddsubtractList(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract);

    /**
     * 新增党组织加减分审核
     * 
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 结果
     */
    public int insertDjOrgAssessmentAddsubtract(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract);

    /**
     * 修改党组织加减分审核
     * 
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 结果
     */
    public int updateDjOrgAssessmentAddsubtract(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract);

    /**
     * 删除党组织加减分审核
     * 
     * @param addsubtractId 党组织加减分审核ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentAddsubtractById(Long addsubtractId);

    /**
     * 批量删除党组织加减分审核
     * 
     * @param addsubtractIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentAddsubtractByIds(Long[] addsubtractIds);
}
