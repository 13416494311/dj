package com.ruoyi.project.members.mapper;

import java.util.List;
import com.ruoyi.project.members.domain.DjPartyMemberSuggestions;

/**
 * 党员建议Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
public interface DjPartyMemberSuggestionsMapper 
{
    /**
     * 查询党员建议
     * 
     * @param suggestionsId 党员建议ID
     * @return 党员建议
     */
    public DjPartyMemberSuggestions selectDjPartyMemberSuggestionsById(Long suggestionsId);

    /**
     * 查询党员建议列表
     * 
     * @param djPartyMemberSuggestions 党员建议
     * @return 党员建议集合
     */
    public List<DjPartyMemberSuggestions> selectDjPartyMemberSuggestionsList(DjPartyMemberSuggestions djPartyMemberSuggestions);

    /**
     * 新增党员建议
     * 
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    public int insertDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions);

    /**
     * 修改党员建议
     * 
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    public int updateDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions);

    /**
     * 删除党员建议
     * 
     * @param suggestionsId 党员建议ID
     * @return 结果
     */
    public int deleteDjPartyMemberSuggestionsById(Long suggestionsId);

    /**
     * 批量删除党员建议
     * 
     * @param suggestionsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberSuggestionsByIds(Long[] suggestionsIds);
}
