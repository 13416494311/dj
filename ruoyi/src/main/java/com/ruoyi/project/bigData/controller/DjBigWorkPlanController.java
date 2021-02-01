package com.ruoyi.project.bigData.controller;

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
import com.ruoyi.project.bigData.domain.DjBigWorkPlan;
import com.ruoyi.project.bigData.service.IDjBigWorkPlanService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党建工作计划Controller
 * 
 * @author ruoyi
 * @date 2021-01-31
 */
@RestController
@RequestMapping("/bigData/workPlan")
public class DjBigWorkPlanController extends BaseController
{
    @Autowired
    private IDjBigWorkPlanService djBigWorkPlanService;

    /**
     * 查询党建工作计划列表
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjBigWorkPlan djBigWorkPlan)
    {
        startPage();
        List<DjBigWorkPlan> list = djBigWorkPlanService.selectDjBigWorkPlanList(djBigWorkPlan);
        return getDataTable(list);
    }

    /**
     * 导出党建工作计划列表
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:export')")
    @Log(title = "党建工作计划", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjBigWorkPlan djBigWorkPlan)
    {
        List<DjBigWorkPlan> list = djBigWorkPlanService.selectDjBigWorkPlanList(djBigWorkPlan);
        ExcelUtil<DjBigWorkPlan> util = new ExcelUtil<DjBigWorkPlan>(DjBigWorkPlan.class);
        return util.exportExcel(list, "workPlan");
    }

    /**
     * 获取党建工作计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djBigWorkPlanService.selectDjBigWorkPlanById(id));
    }

    /**
     * 新增党建工作计划
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:add')")
    @Log(title = "党建工作计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjBigWorkPlan djBigWorkPlan)
    {
        return toAjax(djBigWorkPlanService.insertDjBigWorkPlan(djBigWorkPlan));
    }

    /**
     * 修改党建工作计划
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:edit')")
    @Log(title = "党建工作计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjBigWorkPlan djBigWorkPlan)
    {
        return toAjax(djBigWorkPlanService.updateDjBigWorkPlan(djBigWorkPlan));
    }

    /**
     * 删除党建工作计划
     */
    @PreAuthorize("@ss.hasPermi('bigData:workPlan:remove')")
    @Log(title = "党建工作计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djBigWorkPlanService.deleteDjBigWorkPlanByIds(ids));
    }
}
