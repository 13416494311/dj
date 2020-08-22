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
import com.ruoyi.project.activity.domain.DjActivitySummary;
import com.ruoyi.project.activity.service.IDjActivitySummaryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动纪要Controller
 * 
 * @author ruoyi
 * @date 2020-08-22
 */
@RestController
@RequestMapping("/activity/summary")
public class DjActivitySummaryController extends BaseController
{
    @Autowired
    private IDjActivitySummaryService djActivitySummaryService;

    /**
     * 查询活动纪要列表
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjActivitySummary djActivitySummary)
    {
        startPage();
        List<DjActivitySummary> list = djActivitySummaryService.selectDjActivitySummaryList(djActivitySummary);
        return getDataTable(list);
    }

    /**
     * 导出活动纪要列表
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:export')")
    @Log(title = "活动纪要", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivitySummary djActivitySummary)
    {
        List<DjActivitySummary> list = djActivitySummaryService.selectDjActivitySummaryList(djActivitySummary);
        ExcelUtil<DjActivitySummary> util = new ExcelUtil<DjActivitySummary>(DjActivitySummary.class);
        return util.exportExcel(list, "summary");
    }

    /**
     * 获取活动纪要详细信息
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:query')")
    @GetMapping(value = "/{summaryId}")
    public AjaxResult getInfo(@PathVariable("summaryId") Long summaryId)
    {
        return AjaxResult.success(djActivitySummaryService.selectDjActivitySummaryById(summaryId));
    }

    /**
     * 新增活动纪要
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:add')")
    @Log(title = "活动纪要", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivitySummary djActivitySummary)
    {
        return toAjax(djActivitySummaryService.insertDjActivitySummary(djActivitySummary));
    }

    /**
     * 修改活动纪要
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:edit')")
    @Log(title = "活动纪要", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivitySummary djActivitySummary)
    {
        return toAjax(djActivitySummaryService.updateDjActivitySummary(djActivitySummary));
    }

    /**
     * 删除活动纪要
     */
    @PreAuthorize("@ss.hasPermi('activity:summary:remove')")
    @Log(title = "活动纪要", businessType = BusinessType.DELETE)
	@DeleteMapping("/{summaryIds}")
    public AjaxResult remove(@PathVariable Long[] summaryIds)
    {
        return toAjax(djActivitySummaryService.deleteDjActivitySummaryByIds(summaryIds));
    }
}
