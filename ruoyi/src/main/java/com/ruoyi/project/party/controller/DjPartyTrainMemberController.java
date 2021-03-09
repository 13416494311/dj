package com.ruoyi.project.party.controller;

import java.util.List;

import com.ruoyi.project.activity.domain.DjActivityMember;
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
import com.ruoyi.project.party.domain.DjPartyTrainMember;
import com.ruoyi.project.party.service.IDjPartyTrainMemberService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 参与培训党员Controller
 *
 * @author ruoyi
 * @date 2021-03-09
 */
@RestController
@RequestMapping("/party/trainMember")
public class DjPartyTrainMemberController extends BaseController
{
    @Autowired
    private IDjPartyTrainMemberService djPartyTrainMemberService;

    /**
     * 查询参与培训党员列表
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyTrainMember djPartyTrainMember)
    {
        startPage();
        List<DjPartyTrainMember> list = djPartyTrainMemberService.selectDjPartyTrainMemberList(djPartyTrainMember);
        return getDataTable(list);
    }

    /**
     * 导出参与培训党员列表
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:export')")
    @Log(title = "参与培训党员", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyTrainMember djPartyTrainMember)
    {
        List<DjPartyTrainMember> list = djPartyTrainMemberService.selectDjPartyTrainMemberList(djPartyTrainMember);
        ExcelUtil<DjPartyTrainMember> util = new ExcelUtil<DjPartyTrainMember>(DjPartyTrainMember.class);
        return util.exportExcel(list, "trainMember");
    }

    /**
     * 获取参与培训党员详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:query')")
    @GetMapping(value = "/{trainMemberId}")
    public AjaxResult getInfo(@PathVariable("trainMemberId") Long trainMemberId)
    {
        return AjaxResult.success(djPartyTrainMemberService.selectDjPartyTrainMemberById(trainMemberId));
    }

    /**
     * 新增参与培训党员
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:add')")
    @Log(title = "参与培训党员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyTrainMember djPartyTrainMember)
    {
        return toAjax(djPartyTrainMemberService.insertDjPartyTrainMember(djPartyTrainMember));
    }

    @PreAuthorize("@ss.hasPermi('party:trainMember:add')")
    @Log(title = "参与培训党员", businessType = BusinessType.UPDATE)
    @PostMapping("/addTrainMembers")
    public AjaxResult addMembers(String trainMemberIds,String trainUuid,String partyOrgId)
    {
        for(String trainMemberId:trainMemberIds.split(",")){
            DjPartyTrainMember djPartyTrainMember = new DjPartyTrainMember();
            djPartyTrainMember.setPartyMemberId(Long.parseLong(trainMemberId));
            djPartyTrainMember.setTrainUuid(trainUuid);
            djPartyTrainMember.setPartyOrgId(Long.valueOf(partyOrgId));
            djPartyTrainMember.setStatus("1");
            djPartyTrainMemberService.insertDjPartyTrainMember(djPartyTrainMember);
        }
        return AjaxResult.success();
    }

    /**
     * 修改参与培训党员
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:edit')")
    @Log(title = "参与培训党员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyTrainMember djPartyTrainMember)
    {
        return toAjax(djPartyTrainMemberService.updateDjPartyTrainMember(djPartyTrainMember));
    }

    @PreAuthorize("@ss.hasPermi('party:trainMember:edit')")
    @Log(title = "参与培训党员", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMembers")
    public AjaxResult updateMembers(String trainMemberIds,String status)
    {
        for(String trainMemberId:trainMemberIds.split(",")){
            DjPartyTrainMember djPartyTrainMember = new DjPartyTrainMember();
            djPartyTrainMember.setTrainMemberId(Long.parseLong(trainMemberId));
            djPartyTrainMember.setStatus(status);
            djPartyTrainMemberService.updateDjPartyTrainMember(djPartyTrainMember);
        }
        return AjaxResult.success();
    }

    /**
     * 删除参与培训党员
     */
    @PreAuthorize("@ss.hasPermi('party:trainMember:remove')")
    @Log(title = "参与培训党员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{trainMemberIds}")
    public AjaxResult remove(@PathVariable Long[] trainMemberIds)
    {
        return toAjax(djPartyTrainMemberService.deleteDjPartyTrainMemberByIds(trainMemberIds));
    }
}
