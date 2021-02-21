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
import com.ruoyi.project.party.domain.DjPartyChangeDetail;
import com.ruoyi.project.party.service.IDjPartyChangeDetailService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 换届职位详情Controller
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@RestController
@RequestMapping("/party/changeDetail")
public class DjPartyChangeDetailController extends BaseController
{
    @Autowired
    private IDjPartyChangeDetailService djPartyChangeDetailService;

    /**
     * 查询换届职位详情列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjPartyChangeDetail djPartyChangeDetail)
    {
        startPage();
        List<DjPartyChangeDetail> list = djPartyChangeDetailService.selectDjPartyChangeDetailList(djPartyChangeDetail);
        return getDataTable(list);
    }

    /**
     * 导出换届职位详情列表
     */
    @Log(title = "换届职位详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyChangeDetail djPartyChangeDetail)
    {
        List<DjPartyChangeDetail> list = djPartyChangeDetailService.selectDjPartyChangeDetailList(djPartyChangeDetail);
        ExcelUtil<DjPartyChangeDetail> util = new ExcelUtil<DjPartyChangeDetail>(DjPartyChangeDetail.class);
        return util.exportExcel(list, "changeDetail");
    }

    /**
     * 获取换届职位详情详细信息
     */
    @GetMapping(value = "/{changeDetailId}")
    public AjaxResult getInfo(@PathVariable("changeDetailId") Long changeDetailId)
    {
        return AjaxResult.success(djPartyChangeDetailService.selectDjPartyChangeDetailById(changeDetailId));
    }

    /**
     * 新增换届职位详情
     */
    @Log(title = "换届职位详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyChangeDetail djPartyChangeDetail)
    {
        return toAjax(djPartyChangeDetailService.insertDjPartyChangeDetail(djPartyChangeDetail));
    }

    /**
     * 修改换届职位详情
     */
    @Log(title = "换届职位详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyChangeDetail djPartyChangeDetail)
    {
        return toAjax(djPartyChangeDetailService.updateDjPartyChangeDetail(djPartyChangeDetail));
    }

    /**
     * 删除换届职位详情
     */
    @Log(title = "换届职位详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{changeDetailIds}")
    public AjaxResult remove(@PathVariable Long[] changeDetailIds)
    {
        return toAjax(djPartyChangeDetailService.deleteDjPartyChangeDetailByIds(changeDetailIds));
    }
}
