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
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.service.IDjActivityMemberService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动参与人Controller
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@RestController
@RequestMapping("/activity/member")
public class DjActivityMemberController extends BaseController
{
    @Autowired
    private IDjActivityMemberService djActivityMemberService;

    /**
     * 查询活动参与人列表
     */
    @PreAuthorize("@ss.hasPermi('activity:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjActivityMember djActivityMember)
    {
        startPage();
        List<DjActivityMember> list = djActivityMemberService.selectDjActivityMemberList(djActivityMember);
        return getDataTable(list);
    }

    /**
     * 导出活动参与人列表
     */
    @PreAuthorize("@ss.hasPermi('activity:member:export')")
    @Log(title = "活动参与人", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityMember djActivityMember)
    {
        List<DjActivityMember> list = djActivityMemberService.selectDjActivityMemberList(djActivityMember);
        ExcelUtil<DjActivityMember> util = new ExcelUtil<DjActivityMember>(DjActivityMember.class);
        return util.exportExcel(list, "member");
    }

    /**
     * 获取活动参与人详细信息
     */
    @PreAuthorize("@ss.hasPermi('activity:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(djActivityMemberService.selectDjActivityMemberById(memberId));
    }

    /**
     * 新增活动参与人
     */
    @PreAuthorize("@ss.hasPermi('activity:member:add')")
    @Log(title = "活动参与人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityMember djActivityMember)
    {
        return toAjax(djActivityMemberService.insertDjActivityMember(djActivityMember));
    }

    /**
     * 新增活动参与人
     */

    @Log(title = "活动参与人", businessType = BusinessType.INSERT)
    @PostMapping("/addByArrange")
    public AjaxResult addByArrange(String planUuid,String partyOrgId,String partyMemberIds)
    {
        for(String partyMemberId:partyMemberIds.split(",")){
            DjActivityMember activityMember = new DjActivityMember();
            activityMember.setPlanUuid(planUuid);
            activityMember.setPartyOrgId(Long.parseLong(partyOrgId));
            activityMember.setPartyMemberId(Long.parseLong(partyMemberId));
            activityMember.setStatus("1");
            djActivityMemberService.insertDjActivityMember(activityMember);
        }
        return AjaxResult.success();
    }
    /**
     * 修改活动参与人
     */
    @PreAuthorize("@ss.hasPermi('activity:member:edit')")
    @Log(title = "活动参与人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityMember djActivityMember)
    {
        return toAjax(djActivityMemberService.updateDjActivityMember(djActivityMember));
    }

    /**
     * 删除活动参与人
     */
    @PreAuthorize("@ss.hasPermi('activity:member:remove')")
    @Log(title = "活动参与人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(djActivityMemberService.deleteDjActivityMemberByIds(memberIds));
    }
}
