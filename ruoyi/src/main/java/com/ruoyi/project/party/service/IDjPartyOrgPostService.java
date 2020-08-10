package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyOrgPost;

/**
 * 党组织职务Service接口
 *
 * @author ruoyi
 * @date 2020-08-08
 */
public interface IDjPartyOrgPostService
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
     * 批量删除党组织职务
     *
     * @param ids 需要删除的党组织职务ID
     * @return 结果
     */
    public int deleteDjPartyOrgPostByIds(Long[] ids);

    /**
     * 删除党组织职务信息
     *
     * @param id 党组织职务ID
     * @return 结果
     */
    public int deleteDjPartyOrgPostById(Long id);
}
