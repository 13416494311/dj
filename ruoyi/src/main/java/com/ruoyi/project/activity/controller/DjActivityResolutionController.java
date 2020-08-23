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
import com.ruoyi.project.activity.domain.DjActivityResolution;
import com.ruoyi.project.activity.service.IDjActivityResolutionService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动决议Controller
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@RestController
@RequestMapping("/activity/resolution")
public class DjActivityResolutionController extends BaseController
{
    @Autowired
    private IDjActivityResolutionService djActivityResolutionService;

    /**
     * 查询活动决议列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivityResolution djActivityResolution)
    {
        startPage();
        List<DjActivityResolution> list = djActivityResolutionService.selectDjActivityResolutionList(djActivityResolution);
        return getDataTable(list);
    }

    /**
     * 导出活动决议列表
     */
    @Log(title = "活动决议", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityResolution djActivityResolution)
    {
        List<DjActivityResolution> list = djActivityResolutionService.selectDjActivityResolutionList(djActivityResolution);
        ExcelUtil<DjActivityResolution> util = new ExcelUtil<DjActivityResolution>(DjActivityResolution.class);
        return util.exportExcel(list, "resolution");
    }

    /**
     * 获取活动决议详细信息
     */
    @GetMapping(value = "/{resolutionId}")
    public AjaxResult getInfo(@PathVariable("resolutionId") Long resolutionId)
    {
        return AjaxResult.success(djActivityResolutionService.selectDjActivityResolutionById(resolutionId));
    }

    /**
     * 新增活动决议
     */
    @Log(title = "活动决议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityResolution djActivityResolution)
    {
        return toAjax(djActivityResolutionService.insertDjActivityResolution(djActivityResolution));
    }

    /**
     * 修改活动决议
     */
    @Log(title = "活动决议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityResolution djActivityResolution)
    {
        return toAjax(djActivityResolutionService.updateDjActivityResolution(djActivityResolution));
    }

    /**
     * 删除活动决议
     */
    @Log(title = "活动决议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{resolutionIds}")
    public AjaxResult remove(@PathVariable Long[] resolutionIds)
    {
        return toAjax(djActivityResolutionService.deleteDjActivityResolutionByIds(resolutionIds));
    }
}
