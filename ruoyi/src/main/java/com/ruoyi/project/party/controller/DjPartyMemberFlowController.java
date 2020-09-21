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
import com.ruoyi.project.party.domain.DjPartyMemberFlow;
import com.ruoyi.project.party.service.IDjPartyMemberFlowService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员流动Controller
 *
 * @author ruoyi
 * @date 2020-09-21
 */
@RestController
@RequestMapping("/party/memberFlow")
public class DjPartyMemberFlowController extends BaseController
{
    @Autowired
    private IDjPartyMemberFlowService djPartyMemberFlowService;

    /**
     * 查询党员流动列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberFlow djPartyMemberFlow)
    {
        startPage();
        List<DjPartyMemberFlow> list = djPartyMemberFlowService.selectDjPartyMemberFlowList(djPartyMemberFlow);
        return getDataTable(list);
    }

    /**
     * 导出党员流动列表
     */
    @Log(title = "党员流动", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberFlow djPartyMemberFlow)
    {
        List<DjPartyMemberFlow> list = djPartyMemberFlowService.selectDjPartyMemberFlowList(djPartyMemberFlow);
        ExcelUtil<DjPartyMemberFlow> util = new ExcelUtil<DjPartyMemberFlow>(DjPartyMemberFlow.class);
        return util.exportExcel(list, "memberFlow");
    }

    /**
     * 获取党员流动详细信息
     */
    @GetMapping(value = "/{flowId}")
    public AjaxResult getInfo(@PathVariable("flowId") Long flowId)
    {
        return AjaxResult.success(djPartyMemberFlowService.selectDjPartyMemberFlowById(flowId));
    }

    /**
     * 新增党员流动
     */
    @Log(title = "党员流动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberFlow djPartyMemberFlow)
    {
        return toAjax(djPartyMemberFlowService.insertDjPartyMemberFlow(djPartyMemberFlow));
    }

    /**
     * 修改党员流动
     */
    @Log(title = "党员流动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberFlow djPartyMemberFlow)
    {
        return toAjax(djPartyMemberFlowService.updateDjPartyMemberFlow(djPartyMemberFlow));
    }

    /**
     * 删除党员流动
     */
    @Log(title = "党员流动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{flowIds}")
    public AjaxResult remove(@PathVariable Long[] flowIds)
    {
        return toAjax(djPartyMemberFlowService.deleteDjPartyMemberFlowByIds(flowIds));
    }
}
