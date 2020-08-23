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
import com.ruoyi.project.activity.domain.DjActivitySupervise;
import com.ruoyi.project.activity.service.IDjActivitySuperviseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动督办Controller
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@RestController
@RequestMapping("/activity/supervise")
public class DjActivitySuperviseController extends BaseController
{
    @Autowired
    private IDjActivitySuperviseService djActivitySuperviseService;

    /**
     * 查询活动督办列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivitySupervise djActivitySupervise)
    {
        startPage();
        List<DjActivitySupervise> list = djActivitySuperviseService.selectDjActivitySuperviseList(djActivitySupervise);
        return getDataTable(list);
    }

    /**
     * 导出活动督办列表
     */
    @Log(title = "活动督办", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivitySupervise djActivitySupervise)
    {
        List<DjActivitySupervise> list = djActivitySuperviseService.selectDjActivitySuperviseList(djActivitySupervise);
        ExcelUtil<DjActivitySupervise> util = new ExcelUtil<DjActivitySupervise>(DjActivitySupervise.class);
        return util.exportExcel(list, "supervise");
    }

    /**
     * 获取活动督办详细信息
     */
    @GetMapping(value = "/{superviseId}")
    public AjaxResult getInfo(@PathVariable("superviseId") Long superviseId)
    {
        return AjaxResult.success(djActivitySuperviseService.selectDjActivitySuperviseById(superviseId));
    }

    /**
     * 新增活动督办
     */
    @Log(title = "活动督办", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivitySupervise djActivitySupervise)
    {
        return toAjax(djActivitySuperviseService.insertDjActivitySupervise(djActivitySupervise));
    }

    /**
     * 修改活动督办
     */
    @Log(title = "活动督办", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivitySupervise djActivitySupervise)
    {
        return toAjax(djActivitySuperviseService.updateDjActivitySupervise(djActivitySupervise));
    }

    /**
     * 删除活动督办
     */
    @Log(title = "活动督办", businessType = BusinessType.DELETE)
	@DeleteMapping("/{superviseIds}")
    public AjaxResult remove(@PathVariable Long[] superviseIds)
    {
        return toAjax(djActivitySuperviseService.deleteDjActivitySuperviseByIds(superviseIds));
    }
}
