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
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;
import com.ruoyi.project.party.service.IDjOrgAssessmentyearService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织考核年Controller
 *
 * @author admin
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/party/assessmentyear")
public class DjOrgAssessmentyearController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentyearService djOrgAssessmentyearService;


    /**
     * 查询党组织考核年列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody DjOrgAssessmentyear djOrgAssessmentyear)
    {
        startPage();
        List<DjOrgAssessmentyear> list = djOrgAssessmentyearService.selectDjOrgAssessmentyearList(djOrgAssessmentyear);
        return getDataTable(list);
    }

    /**
     * 导出党组织考核年列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:export')")
    @Log(title = "党组织考核年", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        List<DjOrgAssessmentyear> list = djOrgAssessmentyearService.selectDjOrgAssessmentyearList(djOrgAssessmentyear);
        ExcelUtil<DjOrgAssessmentyear> util = new ExcelUtil<DjOrgAssessmentyear>(DjOrgAssessmentyear.class);
        return util.exportExcel(list, "assessmentyear");
    }

    /**
     * 获取党组织考核年详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentyearService.selectDjOrgAssessmentyearById(id));
    }



    /**
     * 新增党组织考核年
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:add')")
    @Log(title = "党组织考核年", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessmentyear djOrgAssessmentyear)
    {
        return toAjax(djOrgAssessmentyearService.insertDjOrgAssessmentyear(djOrgAssessmentyear));
    }

    /**
     * 修改党组织考核年
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:edit')")
    @Log(title = "党组织考核年", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessmentyear djOrgAssessmentyear)
    {
        return toAjax(djOrgAssessmentyearService.updateDjOrgAssessmentyear(djOrgAssessmentyear));
    }

    /**
     * 删除党组织考核年
     */
    @PreAuthorize("@ss.hasPermi('party:assessmentyear:remove')")
    @Log(title = "党组织考核年", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentyearService.deleteDjOrgAssessmentyearByIds(ids));
    }


}
