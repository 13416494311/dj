package com.ruoyi.project.activity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.activity.domain.DjActivityParams;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.activity.domain.DjActivityArrange;
import com.ruoyi.project.activity.service.IDjActivityArrangeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动安排Controller
 *
 * @author ruoyi
 * @date 2020-08-15
 */
@RestController
@RequestMapping("/activity/arrange")
public class DjActivityArrangeController extends BaseController
{
    @Autowired
    private IDjActivityArrangeService djActivityArrangeService;


    /**
     * 查询活动安排列表
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:list')")
    @GetMapping("/listByParam")
    @DataScope(partyOrgAlias = "arrange")
    public TableDataInfo listByParam(DjActivityParams params)
    {
        startPage();
        List<DjActivityArrange> list = djActivityArrangeService.selectArrangeListByParam(params);
        return getDataTable(list);
    }

    /**
     * 查询活动安排列表
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjActivityArrange djActivityArrange)
    {
        startPage();
        List<DjActivityArrange> list = djActivityArrangeService.selectDjActivityArrangeList(djActivityArrange);
        return getDataTable(list);
    }

    /**
     * 导出活动安排列表
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:export')")
    @Log(title = "活动安排", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityArrange djActivityArrange)
    {
        List<DjActivityArrange> list = djActivityArrangeService.selectDjActivityArrangeList(djActivityArrange);
        ExcelUtil<DjActivityArrange> util = new ExcelUtil<DjActivityArrange>(DjActivityArrange.class);
        return util.exportExcel(list, "arrange");
    }

    /**
     * 获取活动安排详细信息
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djActivityArrangeService.selectDjActivityArrangeById(id));
    }

    /**
     * 新增活动安排
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:add')")
    @Log(title = "活动安排", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityArrange djActivityArrange)
    {
        return toAjax(djActivityArrangeService.insertDjActivityArrange(djActivityArrange));
    }


    /**
     * 新增活动安排
     */
    @Log(title = "活动安排", businessType = BusinessType.INSERT)
    @PostMapping("/addByPlan")
    public AjaxResult addByPlan(String planUuid,String partyOrgIds)
    {
        for(String partyOrgId:partyOrgIds.split(",")){
            DjActivityArrange djActivityArrange =new DjActivityArrange();
            djActivityArrange.setPlanUuid(planUuid);
            djActivityArrange.setPartyOrgId(Long.parseLong(partyOrgId));
            djActivityArrange.setStatus("1");
            djActivityArrangeService.insertDjActivityArrange(djActivityArrange);
        }
        return AjaxResult.success();
    }

    /**
     * 修改活动安排
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:edit')")
    @Log(title = "活动安排", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityArrange djActivityArrange)
    {
        return toAjax(djActivityArrangeService.updateDjActivityArrange(djActivityArrange));
    }

    /**
     * 删除活动安排
     */
    @PreAuthorize("@ss.hasPermi('activity:arrange:remove')")
    @Log(title = "活动安排", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djActivityArrangeService.deleteDjActivityArrangeByIds(ids));
    }


}
