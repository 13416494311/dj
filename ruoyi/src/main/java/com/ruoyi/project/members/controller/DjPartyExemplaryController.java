package com.ruoyi.project.members.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ruoyi.project.members.domain.DjPartyExemplary;
import com.ruoyi.project.members.service.IDjPartyExemplaryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 先锋模范Controller
 *
 * @author ruoyi
 * @date 2021-02-26
 */
@RestController
@RequestMapping("/members/exemplary")
public class DjPartyExemplaryController extends BaseController
{
    @Autowired
    private IDjPartyExemplaryService djPartyExemplaryService;

    /**
     * 查询先锋模范列表
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o", userAlias = "u")
    public TableDataInfo list(DjPartyExemplary djPartyExemplary,String memberName)
    {
        startPage();
        Map<String, Object> params = new HashMap<>();
        params.put("memberName",memberName);
        djPartyExemplary.setParams(params);
        List<DjPartyExemplary> list = djPartyExemplaryService.selectDjPartyExemplaryList(djPartyExemplary);
        return getDataTable(list);
    }

    /**
     * 导出先锋模范列表
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:export')")
    @Log(title = "先锋模范", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyExemplary djPartyExemplary)
    {
        List<DjPartyExemplary> list = djPartyExemplaryService.selectDjPartyExemplaryList(djPartyExemplary);
        ExcelUtil<DjPartyExemplary> util = new ExcelUtil<DjPartyExemplary>(DjPartyExemplary.class);
        return util.exportExcel(list, "exemplary");
    }

    /**
     * 获取先锋模范详细信息
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:query')")
    @GetMapping(value = "/{exemplaryId}")
    public AjaxResult getInfo(@PathVariable("exemplaryId") Long exemplaryId)
    {
        return AjaxResult.success(djPartyExemplaryService.selectDjPartyExemplaryById(exemplaryId));
    }

    /**
     * 新增先锋模范
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:add')")
    @Log(title = "先锋模范", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyExemplary djPartyExemplary)
    {
        return toAjax(djPartyExemplaryService.insertDjPartyExemplary(djPartyExemplary));
    }

    /**
     * 修改先锋模范
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:edit')")
    @Log(title = "先锋模范", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyExemplary djPartyExemplary)
    {
        return toAjax(djPartyExemplaryService.updateDjPartyExemplary(djPartyExemplary));
    }

    /**
     * 删除先锋模范
     */
    @PreAuthorize("@ss.hasPermi('members:exemplary:remove')")
    @Log(title = "先锋模范", businessType = BusinessType.DELETE)
	@DeleteMapping("/{exemplaryIds}")
    public AjaxResult remove(@PathVariable Long[] exemplaryIds)
    {
        return toAjax(djPartyExemplaryService.deleteDjPartyExemplaryByIds(exemplaryIds));
    }
}
