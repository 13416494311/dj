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
import com.ruoyi.project.party.domain.DjOrgAssessmentListScore;
import com.ruoyi.project.party.service.IDjOrgAssessmentListScoreService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 考核评价评分Controller
 *
 * @author admin
 * @date 2021-03-12
 */
@RestController
@RequestMapping("/party/assessmentScore")
public class DjOrgAssessmentListScoreController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentListScoreService djOrgAssessmentListScoreService;

    /**
     * 查询考核评价评分列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        startPage();
        List<DjOrgAssessmentListScore> list = djOrgAssessmentListScoreService.selectDjOrgAssessmentListScoreList(djOrgAssessmentListScore);
        return getDataTable(list);
    }

    /**
     * 导出考核评价评分列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:export')")
    @Log(title = "考核评价评分", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        List<DjOrgAssessmentListScore> list = djOrgAssessmentListScoreService.selectDjOrgAssessmentListScoreList(djOrgAssessmentListScore);
        ExcelUtil<DjOrgAssessmentListScore> util = new ExcelUtil<DjOrgAssessmentListScore>(DjOrgAssessmentListScore.class);
        return util.exportExcel(list, "assessmentScore");
    }

    /**
     * 获取考核评价评分详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentListScoreService.selectDjOrgAssessmentListScoreById(id));
    }

    /**
     * 新增考核评价评分
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:add')")
    @Log(title = "考核评价评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        return toAjax(djOrgAssessmentListScoreService.insertDjOrgAssessmentListScore(djOrgAssessmentListScore));
    }

    /**
     * 修改考核评价评分
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:edit')")
    @Log(title = "考核评价评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentListScore djOrgAssessmentListScore)
    {
        return toAjax(djOrgAssessmentListScoreService.updateDjOrgAssessmentListScore(djOrgAssessmentListScore));
    }

    /**
     * 修改考核评价评分
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:edit')")
    @Log(title = "考核评价评分", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAssessmentScoreList")
    public AjaxResult updateAssessmentScoreList(@RequestBody List<DjOrgAssessmentListScore> djOrgAssessmentListScoreList)
    {
        djOrgAssessmentListScoreList.stream().forEach(djOrgAssessmentListScore->{
            djOrgAssessmentListScoreService.updateDjOrgAssessmentListScore(djOrgAssessmentListScore);
        });
        return AjaxResult.success();
    }

    /**
     * 删除考核评价评分
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentScore:remove')")
    @Log(title = "考核评价评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentListScoreService.deleteDjOrgAssessmentListScoreByIds(ids));
    }
}
