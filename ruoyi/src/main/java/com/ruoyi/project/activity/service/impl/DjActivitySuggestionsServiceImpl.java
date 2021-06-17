package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivitySuggestionsMapper;
import com.ruoyi.project.activity.domain.DjActivitySuggestions;
import com.ruoyi.project.activity.service.IDjActivitySuggestionsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 建言献策Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjActivitySuggestionsServiceImpl implements IDjActivitySuggestionsService
{
    @Autowired
    private DjActivitySuggestionsMapper djActivitySuggestionsMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询建言献策
     *
     * @param suggestionsId 建言献策ID
     * @return 建言献策
     */
    @Override
    public DjActivitySuggestions selectDjActivitySuggestionsById(Long suggestionsId)
    {
        return djActivitySuggestionsMapper.selectDjActivitySuggestionsById(suggestionsId);
    }

    /**
     * 查询建言献策列表
     *
     * @param djActivitySuggestions 建言献策
     * @return 建言献策
     */
    @Override
    public List<DjActivitySuggestions> selectDjActivitySuggestionsList(DjActivitySuggestions djActivitySuggestions)
    {

        List<DjActivitySuggestions> list = djActivitySuggestionsMapper.selectDjActivitySuggestionsList(djActivitySuggestions);
        list.stream().forEach(suggestions->{
            if(StringUtils.isNotNull(suggestions.getPartyMemberId())){
                suggestions.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(suggestions.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(suggestions.getSuggestionsUuid())){
                SysFile sysFile = new SysFile();
                sysFile.setUuid(suggestions.getSuggestionsUuid());
                suggestions.setSysFiles(sysFileService.selectSysFileList(sysFile));
            }
        });

        return list;
    }

    /**
     * 新增建言献策
     *
     * @param djActivitySuggestions 建言献策
     * @return 结果
     */
    @Override
    public int insertDjActivitySuggestions(DjActivitySuggestions djActivitySuggestions)
    {
        djActivitySuggestions.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivitySuggestions.setCreateTime(DateUtils.getNowDate());
        return djActivitySuggestionsMapper.insertDjActivitySuggestions(djActivitySuggestions);
    }

    /**
     * 修改建言献策
     *
     * @param djActivitySuggestions 建言献策
     * @return 结果
     */
    @Override
    public int updateDjActivitySuggestions(DjActivitySuggestions djActivitySuggestions)
    {
        djActivitySuggestions.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivitySuggestions.setUpdateTime(DateUtils.getNowDate());
        return djActivitySuggestionsMapper.updateDjActivitySuggestions(djActivitySuggestions);
    }

    /**
     * 批量删除建言献策
     *
     * @param suggestionsIds 需要删除的建言献策ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuggestionsByIds(Long[] suggestionsIds)
    {
        return djActivitySuggestionsMapper.deleteDjActivitySuggestionsByIds(suggestionsIds);
    }

    /**
     * 删除建言献策信息
     *
     * @param suggestionsId 建言献策ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuggestionsById(Long suggestionsId)
    {
        return djActivitySuggestionsMapper.deleteDjActivitySuggestionsById(suggestionsId);
    }
}
