package com.ruoyi.project.sys.controller;

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
import com.ruoyi.project.sys.domain.DjHistory;
import com.ruoyi.project.sys.service.IDjHistoryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党史Controller
 * 
 * @author ruoyi
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/sys/history")
public class DjHistoryController extends BaseController
{
    @Autowired
    private IDjHistoryService djHistoryService;

    /**
     * 查询党史列表
     */
    @PreAuthorize("@ss.hasPermi('sys:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjHistory djHistory)
    {
        startPage();
        List<DjHistory> list = djHistoryService.selectDjHistoryList(djHistory);
        return getDataTable(list);
    }

    /**
     * 导出党史列表
     */
    @PreAuthorize("@ss.hasPermi('sys:history:export')")
    @Log(title = "党史", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjHistory djHistory)
    {
        List<DjHistory> list = djHistoryService.selectDjHistoryList(djHistory);
        ExcelUtil<DjHistory> util = new ExcelUtil<DjHistory>(DjHistory.class);
        return util.exportExcel(list, "history");
    }

    /**
     * 获取党史详细信息
     */
    @PreAuthorize("@ss.hasPermi('sys:history:query')")
    @GetMapping(value = "/{historyId}")
    public AjaxResult getInfo(@PathVariable("historyId") Long historyId)
    {
        return AjaxResult.success(djHistoryService.selectDjHistoryById(historyId));
    }

    /**
     * 新增党史
     */
    @PreAuthorize("@ss.hasPermi('sys:history:add')")
    @Log(title = "党史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjHistory djHistory)
    {
        return toAjax(djHistoryService.insertDjHistory(djHistory));
    }

    /**
     * 修改党史
     */
    @PreAuthorize("@ss.hasPermi('sys:history:edit')")
    @Log(title = "党史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjHistory djHistory)
    {
        return toAjax(djHistoryService.updateDjHistory(djHistory));
    }

    /**
     * 删除党史
     */
    @PreAuthorize("@ss.hasPermi('sys:history:remove')")
    @Log(title = "党史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{historyIds}")
    public AjaxResult remove(@PathVariable Long[] historyIds)
    {
        return toAjax(djHistoryService.deleteDjHistoryByIds(historyIds));
    }
}
