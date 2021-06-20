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
import com.ruoyi.project.party.domain.DjOrgAssessmentPerformanceScore;
import com.ruoyi.project.party.service.IDjOrgAssessmentPerformanceScoreService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 绩效考核评分Controller
 *
 * @author ruoyi
 * @date 2021-06-20
 */
@RestController
@RequestMapping("/party/performanceScore")
public class DjOrgAssessmentPerformanceScoreController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentPerformanceScoreService djOrgAssessmentPerformanceScoreService;

    /**
     * 查询绩效考核评分列表
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        startPage();
        List<DjOrgAssessmentPerformanceScore> list = djOrgAssessmentPerformanceScoreService.selectDjOrgAssessmentPerformanceScoreList(djOrgAssessmentPerformanceScore);
        return getDataTable(list);
    }

    /**
     * 导出绩效考核评分列表
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:export')")
    @Log(title = "绩效考核评分", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        List<DjOrgAssessmentPerformanceScore> list = djOrgAssessmentPerformanceScoreService.selectDjOrgAssessmentPerformanceScoreList(djOrgAssessmentPerformanceScore);
        ExcelUtil<DjOrgAssessmentPerformanceScore> util = new ExcelUtil<DjOrgAssessmentPerformanceScore>(DjOrgAssessmentPerformanceScore.class);
        return util.exportExcel(list, "performanceScore");
    }

    /**
     * 获取绩效考核评分详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentPerformanceScoreService.selectDjOrgAssessmentPerformanceScoreById(id));
    }

    /**
     * 新增绩效考核评分
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:add')")
    @Log(title = "绩效考核评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        return toAjax(djOrgAssessmentPerformanceScoreService.insertDjOrgAssessmentPerformanceScore(djOrgAssessmentPerformanceScore));
    }

    /**
     * 修改绩效考核评分
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:edit')")
    @Log(title = "绩效考核评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentPerformanceScore djOrgAssessmentPerformanceScore)
    {
        return toAjax(djOrgAssessmentPerformanceScoreService.updateDjOrgAssessmentPerformanceScore(djOrgAssessmentPerformanceScore));
    }

    /**
     * 删除绩效考核评分
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:remove')")
    @Log(title = "绩效考核评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentPerformanceScoreService.deleteDjOrgAssessmentPerformanceScoreByIds(ids));
    }


    /**
     * 获取绩效考核评分详细信息ITEM
     */
    @PreAuthorize("@ss.hasPermi('party:performanceScore:query')")
    @GetMapping(value = "/getScoreItem/{assessmentYearUuid}")
    public AjaxResult getScoreItem(@PathVariable("assessmentYearUuid") String assessmentYearUuid)
    {
        return AjaxResult.success(djOrgAssessmentPerformanceScoreService.getScoreItem(assessmentYearUuid));
    }
}
