package com.ruoyi.project.members.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.members.mapper.DjPartyMemberSuggestionsMapper;
import com.ruoyi.project.members.domain.DjPartyMemberSuggestions;
import com.ruoyi.project.members.service.IDjPartyMemberSuggestionsService;

/**
 * 党员建议Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Service
public class DjPartyMemberSuggestionsServiceImpl implements IDjPartyMemberSuggestionsService
{
    @Autowired
    private DjPartyMemberSuggestionsMapper djPartyMemberSuggestionsMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;


    /**
     * 查询党员建议
     *
     * @param suggestionsId 党员建议ID
     * @return 党员建议
     */
    @Override
    public DjPartyMemberSuggestions selectDjPartyMemberSuggestionsById(Long suggestionsId)
    {
        DjPartyMemberSuggestions suggestions = djPartyMemberSuggestionsMapper.selectDjPartyMemberSuggestionsById(suggestionsId);
        if(suggestions.getMemberId()!=null){
            suggestions.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getMemberId()));
        }
        if(suggestions.getPartyOrgId()!=null){
            suggestions.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(suggestions.getPartyOrgId()));
        }
        if(suggestions.getHandleMemberId()!=null){
            suggestions.setHandleMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getHandleMemberId()));
        }
        return suggestions;
    }

    /**
     * 查询党员建议列表
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 党员建议
     */
    @Override
    public List<DjPartyMemberSuggestions> selectDjPartyMemberSuggestionsList(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        List<DjPartyMemberSuggestions> suggestionsList = djPartyMemberSuggestionsMapper.selectDjPartyMemberSuggestionsList(djPartyMemberSuggestions);
        suggestionsList.stream().forEach(suggestions -> {

            if(suggestions.getMemberId()!=null){
                suggestions.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getMemberId()));
            }
            if(suggestions.getPartyOrgId()!=null){
                suggestions.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(suggestions.getPartyOrgId()));
            }
            if(suggestions.getHandleMemberId()!=null){
                suggestions.setHandleMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getHandleMemberId()));
            }
        });

        return suggestionsList;
    }

    /**
     * 新增党员建议
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        djPartyMemberSuggestions.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberSuggestionsMapper.insertDjPartyMemberSuggestions(djPartyMemberSuggestions);
    }

    /**
     * 修改党员建议
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        djPartyMemberSuggestions.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberSuggestionsMapper.updateDjPartyMemberSuggestions(djPartyMemberSuggestions);
    }

    /**
     * 批量删除党员建议
     *
     * @param suggestionsIds 需要删除的党员建议ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberSuggestionsByIds(Long[] suggestionsIds)
    {
        return djPartyMemberSuggestionsMapper.deleteDjPartyMemberSuggestionsByIds(suggestionsIds);
    }

    /**
     * 删除党员建议信息
     *
     * @param suggestionsId 党员建议ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberSuggestionsById(Long suggestionsId)
    {
        return djPartyMemberSuggestionsMapper.deleteDjPartyMemberSuggestionsById(suggestionsId);
    }
}
