package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;

/**
 * 党组织考核年Service接口
 *
 * @author admin
 * @date 2021-03-10
 */
public interface IDjOrgAssessmentyearService
{
    /**
     * 查询党组织考核年
     *
     * @param id 党组织考核年ID
     * @return 党组织考核年
     */
    public DjOrgAssessmentyear selectDjOrgAssessmentyearById(Long id);

    /**
     * 查询党组织考核年
     *
     * @param assessmentyearUuid 党组织考核年UUID
     * @return 党组织考核年
     */
    public DjOrgAssessmentyear selectDjOrgAssessmentyearByUuid(String assessmentyearUuid);

    /**
     * 查询党组织考核年列表
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 党组织考核年集合
     */
    public List<DjOrgAssessmentyear> selectDjOrgAssessmentyearList(DjOrgAssessmentyear djOrgAssessmentyear);

    /**
     * 新增党组织考核年
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    public int insertDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear);

    /**
     * 修改党组织考核年
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    public int updateDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear);

    /**
     * 批量删除党组织考核年
     *
     * @param ids 需要删除的党组织考核年ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentyearByIds(Long[] ids);

    /**
     * 删除党组织考核年信息
     *
     * @param id 党组织考核年ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentyearById(Long id);
}
