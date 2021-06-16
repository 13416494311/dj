package com.ruoyi.project.party.controller;

import java.util.List;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberDueOrgService;
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
import com.ruoyi.project.party.domain.DjPartyMemberDue;
import com.ruoyi.project.party.service.IDjPartyMemberDueService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员党费Controller
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/party/due")
public class DjPartyMemberDueController extends BaseController
{
    @Autowired
    private IDjPartyMemberDueService djPartyMemberDueService;



    /**
     * 查询党员党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:due:list')")
    @GetMapping("/listDueMember")
    public AjaxResult listDueMember(DjPartyMemberDue djPartyMemberDue)
    {
        List<DjPartyMember> partyMemberList = djPartyMemberDueService.selectDueMemberList(djPartyMemberDue);

        return AjaxResult.success(partyMemberList);
    }

    /**
     * 查询党员党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:due:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberDue djPartyMemberDue)
    {
        startPage();
        List<DjPartyMemberDue> list = djPartyMemberDueService.selectDjPartyMemberDueList(djPartyMemberDue);
        return getDataTable(list);
    }

    /**
     * 导出党员党费列表
     */
    @PreAuthorize("@ss.hasPermi('party:due:export')")
    @Log(title = "党员党费", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberDue djPartyMemberDue)
    {
        List<DjPartyMemberDue> list = djPartyMemberDueService.selectDjPartyMemberDueList(djPartyMemberDue);
        ExcelUtil<DjPartyMemberDue> util = new ExcelUtil<DjPartyMemberDue>(DjPartyMemberDue.class);
        return util.exportExcel(list, "due");
    }

    /**
     * 获取党员党费详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:due:query')")
    @GetMapping(value = "/{dueMemberId}")
    public AjaxResult getInfo(@PathVariable("dueMemberId") Long dueMemberId)
    {
        return AjaxResult.success(djPartyMemberDueService.selectDjPartyMemberDueById(dueMemberId));
    }

    /**
     * 新增党员党费
     */
    @PreAuthorize("@ss.hasPermi('party:due:add')")
    @Log(title = "党员党费", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberDue djPartyMemberDue)
    {
        return toAjax(djPartyMemberDueService.insertDjPartyMemberDue(djPartyMemberDue));
    }

    /**
     * 修改党员党费
     */
    @PreAuthorize("@ss.hasPermi('party:due:edit')")
    @Log(title = "党员党费", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberDue djPartyMemberDue)
    {
        return toAjax(djPartyMemberDueService.updateDjPartyMemberDue(djPartyMemberDue));
    }

    /**
     * 删除党员党费
     */
    @PreAuthorize("@ss.hasPermi('party:due:remove')")
    @Log(title = "党员党费", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dueMemberIds}")
    public AjaxResult remove(@PathVariable Long[] dueMemberIds)
    {
        return toAjax(djPartyMemberDueService.deleteDjPartyMemberDueByIds(dueMemberIds));
    }
}
