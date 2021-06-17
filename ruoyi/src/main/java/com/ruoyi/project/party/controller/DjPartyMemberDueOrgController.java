package com.ruoyi.project.party.controller;

import java.util.List;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
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
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.service.IDjPartyMemberDueOrgService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织党费Controller
 *
 * @author ruoyi
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/party/dueOrg")
public class DjPartyMemberDueOrgController extends BaseController
{
    @Autowired
    private IDjPartyMemberDueOrgService djPartyMemberDueOrgService;

    /**
     * 查询党组织党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:list')")
    @GetMapping("/list")
    public AjaxResult list(DjPartyMemberDueOrg djPartyMemberDueOrg)
    {
        List<DjPartyMemberDueOrg> list = djPartyMemberDueOrgService.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);
        return AjaxResult.success(list);
    }

    /**
     * 查询党组织党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:list')")
    @PostMapping("/page")
    @DataScope( partyOrgAlias = "o" )
    public TableDataInfo page(@RequestBody DjPartyMemberDueOrg djPartyMemberDueOrg)
    {
        startPage();
        List<DjPartyMemberDueOrg> list = djPartyMemberDueOrgService.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);
        return getDataTable(list);
    }

    /**
     * 导出党组织党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:export')")
    @Log(title = "党组织党费", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberDueOrg djPartyMemberDueOrg)
    {
        List<DjPartyMemberDueOrg> list = djPartyMemberDueOrgService.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);
        ExcelUtil<DjPartyMemberDueOrg> util = new ExcelUtil<DjPartyMemberDueOrg>(DjPartyMemberDueOrg.class);
        return util.exportExcel(list, "dueOrg");
    }

    /**
     * 获取党组织党费详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:query')")
    @GetMapping(value = "/{dueOrgId}")
    public AjaxResult getInfo(@PathVariable("dueOrgId") Long dueOrgId)
    {
        return AjaxResult.success(djPartyMemberDueOrgService.selectDjPartyMemberDueOrgById(dueOrgId));
    }

    /**
     * 新增党组织党费
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:add')")
    @Log(title = "党组织党费", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberDueOrg djPartyMemberDueOrg)
    {

        return toAjax(djPartyMemberDueOrgService.insertDjPartyMemberDueOrg(djPartyMemberDueOrg));
    }

    /**
     * 新增党组织党费
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:add')")
    @Log(title = "党组织党费", businessType = BusinessType.INSERT)
    @PostMapping("/addList")
    public AjaxResult addList(String duePlanUuid,String partyOrgIds)
    {
        for(String partyOrgId:partyOrgIds.split(",")){
            DjPartyMemberDueOrg djPartyMemberDueOrg = new DjPartyMemberDueOrg();
            djPartyMemberDueOrg.setDuePlanUuid(duePlanUuid);
            djPartyMemberDueOrg.setPartyOrgId(Long.parseLong(partyOrgId));
            djPartyMemberDueOrg.setStatus("1");
            djPartyMemberDueOrgService.insertDjPartyMemberDueOrg(djPartyMemberDueOrg);
        }
        return AjaxResult.success();
    }

    /**
     * 修改党组织党费
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:edit')")
    @Log(title = "党组织党费", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberDueOrg djPartyMemberDueOrg)
    {
        return toAjax(djPartyMemberDueOrgService.updateDjPartyMemberDueOrg(djPartyMemberDueOrg));
    }

    /**
     * 删除党组织党费
     */
    @PreAuthorize("@ss.hasPermi('party:dueOrg:remove')")
    @Log(title = "党组织党费", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dueOrgIds}")
    public AjaxResult remove(@PathVariable Long[] dueOrgIds)
    {
        return toAjax(djPartyMemberDueOrgService.deleteDjPartyMemberDueOrgByIds(dueOrgIds));
    }
}
