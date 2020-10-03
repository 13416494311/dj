package com.ruoyi.project.party.mapper;

import java.util.List;

import com.ruoyi.project.party.domain.PartyOrgTreeData;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.project.party.domain.DjPartyOrg;

/**
 * 党组织架构Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-03
 */
public interface DjPartyOrgMapper
{
    /**
     * 查询党组织架构
     *
     * @param partyOrgId 党组织架构ID
     * @return 党组织架构
     */
    public DjPartyOrg selectDjPartyOrgById(Long partyOrgId);

    /**
     * 查询党组织架构列表
     *
     * @param djPartyOrg 党组织架构
     * @return 党组织架构集合
     */
    public List<DjPartyOrg> selectDjPartyOrgList(DjPartyOrg djPartyOrg);

    /**
     * 新增党组织架构
     *
     * @param djPartyOrg 党组织架构
     * @return 结果
     */
    public int insertDjPartyOrg(DjPartyOrg djPartyOrg);

    /**
     * 修改党组织架构
     *
     * @param djPartyOrg 党组织架构
     * @return 结果
     */
    public int updateDjPartyOrg(DjPartyOrg djPartyOrg);

    /**
     * 删除党组织架构
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    public int deleteDjPartyOrgById(Long partyOrgId);

    /**
     * 批量删除党组织架构
     *
     * @param partyOrgIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyOrgByIds(Long[] partyOrgIds);

    /**
     * 校验党组织架构名称是否唯一
     *
     * @param partyOrgName 党组织架构名称
     * @param partyOrgId 父党组织架构ID
     * @return 结果
     */
    public DjPartyOrg checkPartyOrgNameUnique(@Param("partyOrgName") String partyOrgName,
                                              @Param("partyOrgId") Long partyOrgId);

    /**
     * 根据ID查询所有子党组织架构（正常状态）
     *
     * @param partyOrgId 党组织架构ID
     * @return 子党组织架构数
     */
    public int selectNormalChildrenPartyOrgById(Long partyOrgId);

    /**
     * 是否存在党组织架构子节点
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    public int hasChildByPartyOrgId(Long partyOrgId);

    /**
     * 党组织架构是否存在党员
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    public int checkOrgExistMember(Long partyOrgId);

    /**
     * 修改所在部门的上级党组织架构状态
     *
     * @param djPartyOrg 党组织架构
     */
    public void updatePartyOrgStatus(DjPartyOrg djPartyOrg);

    /**
     * 修改子元素关系
     *
     * @param partyOrgs 子元素
     * @return 结果
     */
    public int updatePartyOrgChildren(@Param("partyOrgs") List<DjPartyOrg> partyOrgs);
    /**
     * 根据ID查询所有子党组织架构
     *
     * @param partyOrgId 党组织架构ID
     * @return 党组织架构列表
     */
    public List<DjPartyOrg> selectChildrenPartyOrgById(Long partyOrgId);
    /**
     * 获取党组织架构树数据
     *
     * @return
     */
    public List<PartyOrgTreeData> getPartyOrgTreeData(@Param("partyOrgs") List<DjPartyOrg> partyOrgs);
}
