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
import com.ruoyi.project.party.domain.DjPartyMemberEducation;
import com.ruoyi.project.party.service.IDjPartyMemberEducationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 教育经历Controller
 *
 * @author ruoyi
 * @date 2021-04-07
 */
@RestController
@RequestMapping("/party/memberEducation")
public class DjPartyMemberEducationController extends BaseController
{
    @Autowired
    private IDjPartyMemberEducationService djPartyMemberEducationService;

    /**
     * 查询教育经历列表
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:list')")
    @GetMapping("/list")
    public AjaxResult list(DjPartyMemberEducation djPartyMemberEducation)
    {

        List<DjPartyMemberEducation> list = djPartyMemberEducationService.selectDjPartyMemberEducationList(djPartyMemberEducation);
        return AjaxResult.success(list);
    }

    /**
     * 查询教育经历列表
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:list')")
    @GetMapping("/page")
    public TableDataInfo page(DjPartyMemberEducation djPartyMemberEducation)
    {
        startPage();
        List<DjPartyMemberEducation> list = djPartyMemberEducationService.selectDjPartyMemberEducationList(djPartyMemberEducation);
        return getDataTable(list);
    }

    /**
     * 导出教育经历列表
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:export')")
    @Log(title = "教育经历", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberEducation djPartyMemberEducation)
    {
        List<DjPartyMemberEducation> list = djPartyMemberEducationService.selectDjPartyMemberEducationList(djPartyMemberEducation);
        ExcelUtil<DjPartyMemberEducation> util = new ExcelUtil<DjPartyMemberEducation>(DjPartyMemberEducation.class);
        return util.exportExcel(list, "memberEducation");
    }

    /**
     * 获取教育经历详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:query')")
    @GetMapping(value = "/{educationId}")
    public AjaxResult getInfo(@PathVariable("educationId") Long educationId)
    {
        return AjaxResult.success(djPartyMemberEducationService.selectDjPartyMemberEducationById(educationId));
    }

    /**
     * 新增教育经历
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:add')")
    @Log(title = "教育经历", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberEducation djPartyMemberEducation)
    {
        return toAjax(djPartyMemberEducationService.insertDjPartyMemberEducation(djPartyMemberEducation));
    }

    /**
     * 修改教育经历
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:edit')")
    @Log(title = "教育经历", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberEducation djPartyMemberEducation)
    {
        return toAjax(djPartyMemberEducationService.updateDjPartyMemberEducation(djPartyMemberEducation));
    }

    /**
     * 删除教育经历
     */
    @PreAuthorize("@ss.hasPermi('party:memberEducation:remove')")
    @Log(title = "教育经历", businessType = BusinessType.DELETE)
	@DeleteMapping("/{educationIds}")
    public AjaxResult remove(@PathVariable Long[] educationIds)
    {
        return toAjax(djPartyMemberEducationService.deleteDjPartyMemberEducationByIds(educationIds));
    }
}
