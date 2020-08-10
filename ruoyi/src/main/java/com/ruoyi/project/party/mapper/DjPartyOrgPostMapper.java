package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyOrgPost;

/**
 * 党组织职务Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-08
 */
public interface DjPartyOrgPostMapper
{
    /**
     * 查询党组织职务
     *
     * @param id 党组织职务ID
     * @return 党组织职务
     */
    public DjPartyOrgPost selectDjPartyOrgPostById(Long id);

    /**
     * 查询党组织职务列表
     *
     * @param djPartyOrgPost 党组织职务
     * @return 党组织职务集合
     */
    public List<DjPartyOrgPost> selectDjPartyOrgPostList(DjPartyOrgPost djPartyOrgPost);

    /**
     * 新增党组织职务
     *
     * @param djPartyOrgPost 党组织职务
     * @return 结果
     */
    public int insertDjPartyOrgPost(DjPartyOrgPost djPartyOrgPost);

    /**
     * 修改党组织职务
     *
     * @param djPartyOrgPost 党组织职务
     * @return 结果
     */
    public int updateDjPartyOrgPost(DjPartyOrgPost djPartyOrgPost);

    /**
     * 删除党组织职务
     *
     * @param id 党组织职务ID
     * @return 结果
     */
    public int deleteDjPartyOrgPostById(Long id);

    /**
     * 批量删除党组织职务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyOrgPostByIds(Long[] ids);
}
