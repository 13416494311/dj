package com.ruoyi.project.members.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.members.domain.DjPartyMemberSuggestions;
import com.ruoyi.project.members.service.IDjPartyMemberSuggestionsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员建议Controller
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@RestController
@RequestMapping("/members/suggestions")
public class DjPartyMemberSuggestionsController extends BaseController
{
    @Autowired
    private IDjPartyMemberSuggestionsService djPartyMemberSuggestionsService;

    /**
     * 查询党员建议列表
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o", userAlias = "u")
    public TableDataInfo list(DjPartyMemberSuggestions djPartyMemberSuggestions,String memberName)
    {
        startPage();
        Map<String, Object> params = new HashMap<>();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if(!SecurityUtils.isAdmin(user.getUserId()) && !SecurityUtils.isPartyOrgAll()){
            params.put("userId",user.getUserId());
        }
        params.put("memberName",memberName);
        djPartyMemberSuggestions.setParams(params);
        List<DjPartyMemberSuggestions> list = djPartyMemberSuggestionsService.selectDjPartyMemberSuggestionsList(djPartyMemberSuggestions);
        return getDataTable(list);
    }

    /**
     * 导出党员建议列表
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:export')")
    @Log(title = "党员建议", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        List<DjPartyMemberSuggestions> list = djPartyMemberSuggestionsService.selectDjPartyMemberSuggestionsList(djPartyMemberSuggestions);
        ExcelUtil<DjPartyMemberSuggestions> util = new ExcelUtil<DjPartyMemberSuggestions>(DjPartyMemberSuggestions.class);
        return util.exportExcel(list, "suggestions");
    }

    /**
     * 获取党员建议详细信息
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:query')")
    @GetMapping(value = "/{suggestionsId}")
    public AjaxResult getInfo(@PathVariable("suggestionsId") Long suggestionsId)
    {
        return AjaxResult.success(djPartyMemberSuggestionsService.selectDjPartyMemberSuggestionsById(suggestionsId));
    }

    /**
     * 新增党员建议
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:add')")
    @Log(title = "党员建议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        return toAjax(djPartyMemberSuggestionsService.insertDjPartyMemberSuggestions(djPartyMemberSuggestions));
    }

    /**
     * 修改党员建议
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:edit')")
    @Log(title = "党员建议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        return toAjax(djPartyMemberSuggestionsService.updateDjPartyMemberSuggestions(djPartyMemberSuggestions));
    }

    /**
     * 删除党员建议
     */
    @PreAuthorize("@ss.hasPermi('members:suggestions:remove')")
    @Log(title = "党员建议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{suggestionsIds}")
    public AjaxResult remove(@PathVariable Long[] suggestionsIds)
    {
        return toAjax(djPartyMemberSuggestionsService.deleteDjPartyMemberSuggestionsByIds(suggestionsIds));
    }
}
