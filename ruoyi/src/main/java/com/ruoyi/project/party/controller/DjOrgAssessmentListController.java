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
import com.ruoyi.project.party.domain.DjOrgAssessmentList;
import com.ruoyi.project.party.service.IDjOrgAssessmentListService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织考核评价清单Controller
 * 
 * @author admin
 * @date 2021-03-08
 */
@RestController
@RequestMapping("/party/assessmentList")
public class DjOrgAssessmentListController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentListService djOrgAssessmentListService;

    /**
     * 查询党组织考核评价清单列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjOrgAssessmentList djOrgAssessmentList)
    {
        startPage();
        List<DjOrgAssessmentList> list = djOrgAssessmentListService.selectDjOrgAssessmentListList(djOrgAssessmentList);
        return getDataTable(list);
    }

    /**
     * 导出党组织考核评价清单列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:export')")
    @Log(title = "党组织考核评价清单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentList djOrgAssessmentList)
    {
        List<DjOrgAssessmentList> list = djOrgAssessmentListService.selectDjOrgAssessmentListList(djOrgAssessmentList);
        ExcelUtil<DjOrgAssessmentList> util = new ExcelUtil<DjOrgAssessmentList>(DjOrgAssessmentList.class);
        return util.exportExcel(list, "assessmentList");
    }

    /**
     * 获取党组织考核评价清单详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:query')")
    @GetMapping(value = "/{listId}")
    public AjaxResult getInfo(@PathVariable("listId") Long listId)
    {
        return AjaxResult.success(djOrgAssessmentListService.selectDjOrgAssessmentListById(listId));
    }

    /**
     * 新增党组织考核评价清单
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:add')")
    @Log(title = "党组织考核评价清单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentList djOrgAssessmentList)
    {
        return toAjax(djOrgAssessmentListService.insertDjOrgAssessmentList(djOrgAssessmentList));
    }

    /**
     * 修改党组织考核评价清单
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:edit')")
    @Log(title = "党组织考核评价清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentList djOrgAssessmentList)
    {
        return toAjax(djOrgAssessmentListService.updateDjOrgAssessmentList(djOrgAssessmentList));
    }

    /**
     * 删除党组织考核评价清单
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentList:remove')")
    @Log(title = "党组织考核评价清单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{listIds}")
    public AjaxResult remove(@PathVariable Long[] listIds)
    {
        return toAjax(djOrgAssessmentListService.deleteDjOrgAssessmentListByIds(listIds));
    }
}
