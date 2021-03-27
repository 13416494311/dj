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
import com.ruoyi.project.party.domain.DjOrgAssessmentAddsubtract;
import com.ruoyi.project.party.service.IDjOrgAssessmentAddsubtractService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织加减分审核Controller
 * 
 * @author admin
 * @date 2021-03-15
 */
@RestController
@RequestMapping("/party/addsubtract")
public class DjOrgAssessmentAddsubtractController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentAddsubtractService djOrgAssessmentAddsubtractService;

    /**
     * 查询党组织加减分审核列表
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        startPage();
        List<DjOrgAssessmentAddsubtract> list = djOrgAssessmentAddsubtractService.selectDjOrgAssessmentAddsubtractList(djOrgAssessmentAddsubtract);
        return getDataTable(list);
    }

    /**
     * 导出党组织加减分审核列表
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:export')")
    @Log(title = "党组织加减分审核", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        List<DjOrgAssessmentAddsubtract> list = djOrgAssessmentAddsubtractService.selectDjOrgAssessmentAddsubtractList(djOrgAssessmentAddsubtract);
        ExcelUtil<DjOrgAssessmentAddsubtract> util = new ExcelUtil<DjOrgAssessmentAddsubtract>(DjOrgAssessmentAddsubtract.class);
        return util.exportExcel(list, "addsubtract");
    }

    /**
     * 获取党组织加减分审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:query')")
    @GetMapping(value = "/{addsubtractId}")
    public AjaxResult getInfo(@PathVariable("addsubtractId") Long addsubtractId)
    {
        return AjaxResult.success(djOrgAssessmentAddsubtractService.selectDjOrgAssessmentAddsubtractById(addsubtractId));
    }

    /**
     * 新增党组织加减分审核
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:add')")
    @Log(title = "党组织加减分审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        return toAjax(djOrgAssessmentAddsubtractService.insertDjOrgAssessmentAddsubtract(djOrgAssessmentAddsubtract));
    }

    /**
     * 修改党组织加减分审核
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:edit')")
    @Log(title = "党组织加减分审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        return toAjax(djOrgAssessmentAddsubtractService.updateDjOrgAssessmentAddsubtract(djOrgAssessmentAddsubtract));
    }

    /**
     * 删除党组织加减分审核
     */
    @PreAuthorize("@ss.hasPermi('party:addsubtract:remove')")
    @Log(title = "党组织加减分审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{addsubtractIds}")
    public AjaxResult remove(@PathVariable Long[] addsubtractIds)
    {
        return toAjax(djOrgAssessmentAddsubtractService.deleteDjOrgAssessmentAddsubtractByIds(addsubtractIds));
    }
}
