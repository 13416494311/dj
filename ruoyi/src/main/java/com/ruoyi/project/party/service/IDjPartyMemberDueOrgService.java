package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;

/**
 * 党组织党费Service接口
 * 
 * @author ruoyi
 * @date 2021-06-15
 */
public interface IDjPartyMemberDueOrgService 
{
    /**
     * 查询党组织党费
     * 
     * @param dueOrgId 党组织党费ID
     * @return 党组织党费
     */
    public DjPartyMemberDueOrg selectDjPartyMemberDueOrgById(Long dueOrgId);

    /**
     * 查询党组织党费列表
     * 
     * @param djPartyMemberDueOrg 党组织党费
     * @return 党组织党费集合
     */
    public List<DjPartyMemberDueOrg> selectDjPartyMemberDueOrgList(DjPartyMemberDueOrg djPartyMemberDueOrg);

    /**
     * 新增党组织党费
     * 
     * @param djPartyMemberDueOrg 党组织党费
     * @return 结果
     */
    public int insertDjPartyMemberDueOrg(DjPartyMemberDueOrg djPartyMemberDueOrg);

    /**
     * 修改党组织党费
     * 
     * @param djPartyMemberDueOrg 党组织党费
     * @return 结果
     */
    public int updateDjPartyMemberDueOrg(DjPartyMemberDueOrg djPartyMemberDueOrg);

    /**
     * 批量删除党组织党费
     * 
     * @param dueOrgIds 需要删除的党组织党费ID
     * @return 结果
     */
    public int deleteDjPartyMemberDueOrgByIds(Long[] dueOrgIds);

    /**
     * 删除党组织党费信息
     * 
     * @param dueOrgId 党组织党费ID
     * @return 结果
     */
    public int deleteDjPartyMemberDueOrgById(Long dueOrgId);
}
