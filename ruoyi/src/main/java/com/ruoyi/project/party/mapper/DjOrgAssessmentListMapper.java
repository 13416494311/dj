package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjOrgAssessmentList;

/**
 * 党组织考核评价清单Mapper接口
 * 
 * @author admin
 * @date 2021-03-08
 */
public interface DjOrgAssessmentListMapper 
{
    /**
     * 查询党组织考核评价清单
     * 
     * @param listId 党组织考核评价清单ID
     * @return 党组织考核评价清单
     */
    public DjOrgAssessmentList selectDjOrgAssessmentListById(Long listId);

    /**
     * 查询党组织考核评价清单列表
     * 
     * @param djOrgAssessmentList 党组织考核评价清单
     * @return 党组织考核评价清单集合
     */
    public List<DjOrgAssessmentList> selectDjOrgAssessmentListList(DjOrgAssessmentList djOrgAssessmentList);

    /**
     * 新增党组织考核评价清单
     * 
     * @param djOrgAssessmentList 党组织考核评价清单
     * @return 结果
     */
    public int insertDjOrgAssessmentList(DjOrgAssessmentList djOrgAssessmentList);

    /**
     * 修改党组织考核评价清单
     * 
     * @param djOrgAssessmentList 党组织考核评价清单
     * @return 结果
     */
    public int updateDjOrgAssessmentList(DjOrgAssessmentList djOrgAssessmentList);

    /**
     * 删除党组织考核评价清单
     * 
     * @param listId 党组织考核评价清单ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentListById(Long listId);

    /**
     * 批量删除党组织考核评价清单
     * 
     * @param listIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjOrgAssessmentListByIds(Long[] listIds);
}
