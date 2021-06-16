package com.ruoyi.project.party.controller;

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
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;
import com.ruoyi.project.party.service.IDjPartyMemberDuePlanService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党费计划Controller
 * 
 * @author wandengfeng
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/party/duePlan")
public class DjPartyMemberDuePlanController extends BaseController
{
    @Autowired
    private IDjPartyMemberDuePlanService djPartyMemberDuePlanService;

    /**
     * 查询党费计划列表
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        startPage();
        List<DjPartyMemberDuePlan> list = djPartyMemberDuePlanService.selectDjPartyMemberDuePlanList(djPartyMemberDuePlan);
        return getDataTable(list);
    }

    /**
     * 导出党费计划列表
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:export')")
    @Log(title = "党费计划", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        List<DjPartyMemberDuePlan> list = djPartyMemberDuePlanService.selectDjPartyMemberDuePlanList(djPartyMemberDuePlan);
        ExcelUtil<DjPartyMemberDuePlan> util = new ExcelUtil<DjPartyMemberDuePlan>(DjPartyMemberDuePlan.class);
        return util.exportExcel(list, "duePlan");
    }

    /**
     * 获取党费计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:query')")
    @GetMapping(value = "/{duePlanId}")
    public AjaxResult getInfo(@PathVariable("duePlanId") Long duePlanId)
    {
        return AjaxResult.success(djPartyMemberDuePlanService.selectDjPartyMemberDuePlanById(duePlanId));
    }

    /**
     * 新增党费计划
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:add')")
    @Log(title = "党费计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        return toAjax(djPartyMemberDuePlanService.insertDjPartyMemberDuePlan(djPartyMemberDuePlan));
    }

    /**
     * 修改党费计划
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:edit')")
    @Log(title = "党费计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        return toAjax(djPartyMemberDuePlanService.updateDjPartyMemberDuePlan(djPartyMemberDuePlan));
    }

    /**
     * 删除党费计划
     */
    @PreAuthorize("@ss.hasPermi('party:duePlan:remove')")
    @Log(title = "党费计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{duePlanIds}")
    public AjaxResult remove(@PathVariable Long[] duePlanIds)
    {
        return toAjax(djPartyMemberDuePlanService.deleteDjPartyMemberDuePlanByIds(duePlanIds));
    }
}
