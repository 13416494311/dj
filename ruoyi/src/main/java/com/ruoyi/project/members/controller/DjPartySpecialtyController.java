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
import com.ruoyi.project.members.domain.DjPartySpecialty;
import com.ruoyi.project.members.service.IDjPartySpecialtyService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员特长Controller
 *
 * @author admin
 * @date 2021-02-19
 */
@RestController
@RequestMapping("/members/specialty")
public class DjPartySpecialtyController extends BaseController
{
    @Autowired
    private IDjPartySpecialtyService djPartySpecialtyService;

    /**
     * 查询党员特长列表
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o")
    public TableDataInfo list(DjPartySpecialty djPartySpecialty,String memberName)
    {
        startPage();
        Map<String, Object> params = new HashMap<>();
        params.put("memberName",memberName);
        djPartySpecialty.setParams(params);
        List<DjPartySpecialty> list = djPartySpecialtyService.selectDjPartySpecialtyList(djPartySpecialty);
        return getDataTable(list);
    }

    /**
     * 导出党员特长列表
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:export')")
    @Log(title = "党员特长", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartySpecialty djPartySpecialty)
    {
        List<DjPartySpecialty> list = djPartySpecialtyService.selectDjPartySpecialtyList(djPartySpecialty);
        ExcelUtil<DjPartySpecialty> util = new ExcelUtil<DjPartySpecialty>(DjPartySpecialty.class);
        return util.exportExcel(list, "specialty");
    }

    /**
     * 获取党员特长详细信息
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:query')")
    @GetMapping(value = "/{specialtyId}")
    public AjaxResult getInfo(@PathVariable("specialtyId") Long specialtyId)
    {
        return AjaxResult.success(djPartySpecialtyService.selectDjPartySpecialtyById(specialtyId));
    }

    /**
     * 新增党员特长
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:add')")
    @Log(title = "党员特长", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartySpecialty djPartySpecialty)
    {
        return toAjax(djPartySpecialtyService.insertDjPartySpecialty(djPartySpecialty));
    }

    /**
     * 修改党员特长
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:edit')")
    @Log(title = "党员特长", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartySpecialty djPartySpecialty)
    {
        return toAjax(djPartySpecialtyService.updateDjPartySpecialty(djPartySpecialty));
    }

    /**
     * 删除党员特长
     */
    @PreAuthorize("@ss.hasPermi('members:specialty:remove')")
    @Log(title = "党员特长", businessType = BusinessType.DELETE)
	@DeleteMapping("/{specialtyIds}")
    public AjaxResult remove(@PathVariable Long[] specialtyIds)
    {
        return toAjax(djPartySpecialtyService.deleteDjPartySpecialtyByIds(specialtyIds));
    }
}
