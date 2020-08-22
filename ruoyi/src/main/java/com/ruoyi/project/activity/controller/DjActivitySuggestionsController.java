package com.ruoyi.project.activity.controller;

import java.util.List;
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
import com.ruoyi.project.activity.domain.DjActivitySuggestions;
import com.ruoyi.project.activity.service.IDjActivitySuggestionsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 建言献策Controller
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@RestController
@RequestMapping("/activity/suggestions")
public class DjActivitySuggestionsController extends BaseController
{
    @Autowired
    private IDjActivitySuggestionsService djActivitySuggestionsService;

    /**
     * 查询建言献策列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivitySuggestions djActivitySuggestions)
    {
        startPage();
        List<DjActivitySuggestions> list = djActivitySuggestionsService.selectDjActivitySuggestionsList(djActivitySuggestions);
        return getDataTable(list);
    }

    /**
     * 导出建言献策列表
     */
    @Log(title = "建言献策", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivitySuggestions djActivitySuggestions)
    {
        List<DjActivitySuggestions> list = djActivitySuggestionsService.selectDjActivitySuggestionsList(djActivitySuggestions);
        ExcelUtil<DjActivitySuggestions> util = new ExcelUtil<DjActivitySuggestions>(DjActivitySuggestions.class);
        return util.exportExcel(list, "suggestions");
    }

    /**
     * 获取建言献策详细信息
     */
    @GetMapping(value = "/{suggestionsId}")
    public AjaxResult getInfo(@PathVariable("suggestionsId") Long suggestionsId)
    {
        return AjaxResult.success(djActivitySuggestionsService.selectDjActivitySuggestionsById(suggestionsId));
    }

    /**
     * 新增建言献策
     */
    @Log(title = "建言献策", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivitySuggestions djActivitySuggestions)
    {
        return toAjax(djActivitySuggestionsService.insertDjActivitySuggestions(djActivitySuggestions));
    }

    /**
     * 修改建言献策
     */
    @Log(title = "建言献策", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivitySuggestions djActivitySuggestions)
    {
        return toAjax(djActivitySuggestionsService.updateDjActivitySuggestions(djActivitySuggestions));
    }

    /**
     * 删除建言献策
     */
    @Log(title = "建言献策", businessType = BusinessType.DELETE)
	@DeleteMapping("/{suggestionsIds}")
    public AjaxResult remove(@PathVariable Long[] suggestionsIds)
    {
        return toAjax(djActivitySuggestionsService.deleteDjActivitySuggestionsByIds(suggestionsIds));
    }
}
