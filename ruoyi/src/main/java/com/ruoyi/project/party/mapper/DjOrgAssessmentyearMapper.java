package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;

/**
 * 党组织考核年Mapper接口
 * 
 * @author admin
 * @date 2021-03-10
 */
public interface DjOrgAssessmentyearMapper 
{
    /**
     * 查询党组织考核年
     * 
     * @param id 党组织考核年ID
     * @return 党组织考核年
     */
    public DjOrgAssessmentyear selectDjOrgAssessmentyearById(Long id);

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
     * 删除党组织考核年
     * 
     * @param id 党组织考核年ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentyearById(Long id);

    /**
     * 批量删除党组织考核年
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentyearByIds(Long[] ids);
}
