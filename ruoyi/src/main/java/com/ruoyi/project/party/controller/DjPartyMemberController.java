package com.ruoyi.project.party.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 党员信息Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@RestController
@RequestMapping("/party/member")
public class DjPartyMemberController extends BaseController
{
    @Autowired
    private IDjPartyMemberService djPartyMemberService;

    /**
     * 查询党员信息列表
     */
    @PreAuthorize("@ss.hasPermi('party:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMember djPartyMember)
    {
        startPage();
        List<DjPartyMember> list = djPartyMemberService.selectDjPartyMemberList(djPartyMember);
        return getDataTable(list);
    }

    /**
     * 导出党员信息列表
     */
    @PreAuthorize("@ss.hasPermi('party:member:export')")
    @Log(title = "党员信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMember djPartyMember)
    {
        List<DjPartyMember> list = djPartyMemberService.selectDjPartyMemberList(djPartyMember);
        ExcelUtil<DjPartyMember> util = new ExcelUtil<DjPartyMember>(DjPartyMember.class);
        return util.exportExcel(list, "partyMember");
    }

    /**
     * 获取党员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(djPartyMemberService.selectDjPartyMemberById(memberId));
    }

    /**
     * 新增党员信息
     */
    @PreAuthorize("@ss.hasPermi('party:member:add')")
    @Log(title = "党员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMember djPartyMember)
    {
        djPartyMember.setCreateBy(SecurityUtils.getUsername());
        djPartyMember.setCreateTime(DateUtils.getNowDate());
        return toAjax(djPartyMemberService.insertDjPartyMember(djPartyMember));
    }

    /**
     * 修改党员信息
     */
    @PreAuthorize("@ss.hasPermi('party:member:edit')")
    @Log(title = "党员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMember djPartyMember)
    {
        djPartyMember.setUpdateBy(SecurityUtils.getUsername());
        djPartyMember.setUpdateTime(DateUtils.getNowDate());
        return toAjax(djPartyMemberService.updateDjPartyMember(djPartyMember));
    }

    /**
     * 删除党员信息
     */
    @PreAuthorize("@ss.hasPermi('party:member:remove')")
    @Log(title = "党员信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberId}")
    public AjaxResult remove(@PathVariable Long memberId)
    {
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setMemberId(memberId);
        djPartyMember.setDelFlag("1");
        return toAjax(djPartyMemberService.updateDjPartyMember(djPartyMember));
    }

    /**
     * 头像上传
     */
    @Log(title = "党员头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            return AjaxResult.success(avatar);
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }


    /**
     *  检查姓名 手机号 身份证 邮箱等唯一性
     * @param djPartyMember
     * @return
     * @throws IOException
     */
    @PostMapping("/checkPartyMemberUnique")
    public AjaxResult checkPartyMemberUnique(@Validated @RequestBody DjPartyMember djPartyMember) throws IOException
    {

        if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberNameUnique(djPartyMember)))
        {
            return AjaxResult.success("姓名已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberMobileUnique(djPartyMember)))
        {
            return AjaxResult.success("手机号已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberEmailUnique(djPartyMember)))
        {
            return AjaxResult.success("Email已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberIdentityCardUnique(djPartyMember)))
        {
            return AjaxResult.success("身份证号已存在");
        }
        return AjaxResult.success();
    }

}
