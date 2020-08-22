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
import com.ruoyi.project.activity.domain.DjActivityExperience;
import com.ruoyi.project.activity.service.IDjActivityExperienceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 心得体会Controller
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@RestController
@RequestMapping("/activity/experience")
public class DjActivityExperienceController extends BaseController
{
    @Autowired
    private IDjActivityExperienceService djActivityExperienceService;

    /**
     * 查询心得体会列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivityExperience djActivityExperience)
    {
        startPage();
        List<DjActivityExperience> list = djActivityExperienceService.selectDjActivityExperienceList(djActivityExperience);
        return getDataTable(list);
    }

    /**
     * 导出心得体会列表
     */
    @Log(title = "心得体会", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityExperience djActivityExperience)
    {
        List<DjActivityExperience> list = djActivityExperienceService.selectDjActivityExperienceList(djActivityExperience);
        ExcelUtil<DjActivityExperience> util = new ExcelUtil<DjActivityExperience>(DjActivityExperience.class);
        return util.exportExcel(list, "experience");
    }

    /**
     * 获取心得体会详细信息
     */
    @GetMapping(value = "/{experienceId}")
    public AjaxResult getInfo(@PathVariable("experienceId") Long experienceId)
    {
        return AjaxResult.success(djActivityExperienceService.selectDjActivityExperienceById(experienceId));
    }

    /**
     * 新增心得体会
     */
    @Log(title = "心得体会", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityExperience djActivityExperience)
    {
        return toAjax(djActivityExperienceService.insertDjActivityExperience(djActivityExperience));
    }

    /**
     * 修改心得体会
     */
    @Log(title = "心得体会", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityExperience djActivityExperience)
    {
        return toAjax(djActivityExperienceService.updateDjActivityExperience(djActivityExperience));
    }

    /**
     * 删除心得体会
     */
    @Log(title = "心得体会", businessType = BusinessType.DELETE)
	@DeleteMapping("/{experienceIds}")
    public AjaxResult remove(@PathVariable Long[] experienceIds)
    {
        return toAjax(djActivityExperienceService.deleteDjActivityExperienceByIds(experienceIds));
    }
}
