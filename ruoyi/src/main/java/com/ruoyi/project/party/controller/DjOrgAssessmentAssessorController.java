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
import com.ruoyi.project.party.domain.DjOrgAssessmentAssessor;
import com.ruoyi.project.party.service.IDjOrgAssessmentAssessorService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织述职评议Controller
 * 
 * @author admin
 * @date 2021-03-23
 */
@RestController
@RequestMapping("/party/assessor")
public class DjOrgAssessmentAssessorController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentAssessorService djOrgAssessmentAssessorService;

    /**
     * 查询党组织述职评议列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        startPage();
        List<DjOrgAssessmentAssessor> list = djOrgAssessmentAssessorService.selectDjOrgAssessmentAssessorList(djOrgAssessmentAssessor);
        return getDataTable(list);
    }

    /**
     * 导出党组织述职评议列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:export')")
    @Log(title = "党组织述职评议", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        List<DjOrgAssessmentAssessor> list = djOrgAssessmentAssessorService.selectDjOrgAssessmentAssessorList(djOrgAssessmentAssessor);
        ExcelUtil<DjOrgAssessmentAssessor> util = new ExcelUtil<DjOrgAssessmentAssessor>(DjOrgAssessmentAssessor.class);
        return util.exportExcel(list, "assessor");
    }

    /**
     * 获取党组织述职评议详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentAssessorService.selectDjOrgAssessmentAssessorById(id));
    }

    /**
     * 新增党组织述职评议
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:add')")
    @Log(title = "党组织述职评议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        return toAjax(djOrgAssessmentAssessorService.insertDjOrgAssessmentAssessor(djOrgAssessmentAssessor));
    }

    /**
     * 修改党组织述职评议
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:edit')")
    @Log(title = "党组织述职评议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        return toAjax(djOrgAssessmentAssessorService.updateDjOrgAssessmentAssessor(djOrgAssessmentAssessor));
    }

    /**
     * 删除党组织述职评议
     */
    @PreAuthorize("@ss.hasPermi('party:assessor:remove')")
    @Log(title = "党组织述职评议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentAssessorService.deleteDjOrgAssessmentAssessorByIds(ids));
    }
}
