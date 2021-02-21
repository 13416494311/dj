package com.ruoyi.project.party.controller;

import java.util.List;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
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
import com.ruoyi.project.party.domain.DjPartyChange;
import com.ruoyi.project.party.service.IDjPartyChangeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织换届Controller
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@RestController
@RequestMapping("/party/partyChange")
public class DjPartyChangeController extends BaseController
{
    @Autowired
    private IDjPartyChangeService djPartyChangeService;

    /**
     * 查询党组织换届列表
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o")
    public TableDataInfo list(DjPartyChange djPartyChange)
    {
        startPage();
        List<DjPartyChange> list = djPartyChangeService.selectDjPartyChangeList(djPartyChange);
        return getDataTable(list);
    }

    /**
     * 导出党组织换届列表
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:export')")
    @Log(title = "党组织换届", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyChange djPartyChange)
    {
        List<DjPartyChange> list = djPartyChangeService.selectDjPartyChangeList(djPartyChange);
        ExcelUtil<DjPartyChange> util = new ExcelUtil<DjPartyChange>(DjPartyChange.class);
        return util.exportExcel(list, "partyChange");
    }

    /**
     * 获取党组织换届详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:query')")
    @GetMapping(value = "/{changeId}")
    public AjaxResult getInfo(@PathVariable("changeId") Long changeId)
    {
        return AjaxResult.success(djPartyChangeService.selectDjPartyChangeById(changeId));
    }

    /**
     * 新增党组织换届
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:add')")
    @Log(title = "党组织换届", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyChange djPartyChange)
    {
        return toAjax(djPartyChangeService.insertDjPartyChange(djPartyChange));
    }

    /**
     * 修改党组织换届
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:edit')")
    @Log(title = "党组织换届", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyChange djPartyChange)
    {
        return toAjax(djPartyChangeService.updateDjPartyChange(djPartyChange));
    }

    /**
     * 删除党组织换届
     */
    @PreAuthorize("@ss.hasPermi('party:partyChange:remove')")
    @Log(title = "党组织换届", businessType = BusinessType.DELETE)
	@DeleteMapping("/{changeIds}")
    public AjaxResult remove(@PathVariable Long[] changeIds)
    {
        return toAjax(djPartyChangeService.deleteDjPartyChangeByIds(changeIds));
    }
}
