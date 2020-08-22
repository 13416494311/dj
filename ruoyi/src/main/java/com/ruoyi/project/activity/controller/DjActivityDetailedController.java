package com.ruoyi.project.activity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.activity.domain.DjActivityParams;
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
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动详情Controller
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@RestController
@RequestMapping("/activity/detailed")
public class DjActivityDetailedController extends BaseController
{
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;

    /**
     * 查询活动详情列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivityDetailed djActivityDetailed)
    {
        startPage();
        List<DjActivityDetailed> list = djActivityDetailedService.selectDjActivityDetailedList(djActivityDetailed);
        return getDataTable(list);
    }

    /**
     * 查询活动详情列表
     */
    @GetMapping("/listByParam")
    @DataScope(partyOrgAlias = "detailed")
    public TableDataInfo list(DjActivityParams params)
    {
        startPage();
        List<DjActivityDetailed> list = djActivityDetailedService.selectDetailedListByParam(params);
        return getDataTable(list);
    }

    /**
     * 导出活动详情列表
     */
    @Log(title = "活动详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityDetailed djActivityDetailed)
    {
        List<DjActivityDetailed> list = djActivityDetailedService.selectDjActivityDetailedList(djActivityDetailed);
        ExcelUtil<DjActivityDetailed> util = new ExcelUtil<DjActivityDetailed>(DjActivityDetailed.class);
        return util.exportExcel(list, "detailed");
    }

    /**
     * 获取活动详情详细信息
     */
    @GetMapping(value = "/{detailedId}")
    public AjaxResult getInfo(@PathVariable("detailedId") Long detailedId)
    {
        return AjaxResult.success(djActivityDetailedService.selectDjActivityDetailedById(detailedId));
    }

    /**
     * 获取活动详情详细信息
     */
    @GetMapping(value = "/getByUuid/{detailedUuid}")
    public AjaxResult getByUuid(@PathVariable("detailedUuid") String detailedUuid)
    {
        return AjaxResult.success(djActivityDetailedService.selectDjActivityDetailedByDetailedUuid(detailedUuid));
    }

    /**
     * 新增活动详情
     */
    @PreAuthorize("@ss.hasPermi('activity:detailed:add')")
    @Log(title = "活动详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityDetailed djActivityDetailed)
    {
        return toAjax(djActivityDetailedService.insertDjActivityDetailed(djActivityDetailed));
    }

    /**
     * 修改活动详情
     */
    @Log(title = "活动详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityDetailed djActivityDetailed)
    {
        return toAjax(djActivityDetailedService.updateDjActivityDetailed(djActivityDetailed));
    }

    /**
     * 删除活动详情
     */
    @PreAuthorize("@ss.hasPermi('activity:detailed:remove')")
    @Log(title = "活动详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailedIds}")
    public AjaxResult remove(@PathVariable Long[] detailedIds)
    {
        return toAjax(djActivityDetailedService.deleteDjActivityDetailedByIds(detailedIds));
    }
}
