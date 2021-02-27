package com.ruoyi.project.members.controller;

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
import com.ruoyi.project.members.domain.DjPartyMemberHelp;
import com.ruoyi.project.members.service.IDjPartyMemberHelpService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员帮扶Controller
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
@RestController
@RequestMapping("/members/help")
public class DjPartyMemberHelpController extends BaseController
{
    @Autowired
    private IDjPartyMemberHelpService djPartyMemberHelpService;

    /**
     * 查询党员帮扶列表
     */
    @PreAuthorize("@ss.hasPermi('members:help:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberHelp djPartyMemberHelp)
    {
        startPage();
        List<DjPartyMemberHelp> list = djPartyMemberHelpService.selectDjPartyMemberHelpList(djPartyMemberHelp);
        return getDataTable(list);
    }

    /**
     * 导出党员帮扶列表
     */
    @PreAuthorize("@ss.hasPermi('members:help:export')")
    @Log(title = "党员帮扶", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberHelp djPartyMemberHelp)
    {
        List<DjPartyMemberHelp> list = djPartyMemberHelpService.selectDjPartyMemberHelpList(djPartyMemberHelp);
        ExcelUtil<DjPartyMemberHelp> util = new ExcelUtil<DjPartyMemberHelp>(DjPartyMemberHelp.class);
        return util.exportExcel(list, "help");
    }

    /**
     * 获取党员帮扶详细信息
     */
    @PreAuthorize("@ss.hasPermi('members:help:query')")
    @GetMapping(value = "/{helpId}")
    public AjaxResult getInfo(@PathVariable("helpId") Long helpId)
    {
        return AjaxResult.success(djPartyMemberHelpService.selectDjPartyMemberHelpById(helpId));
    }

    /**
     * 新增党员帮扶
     */
    @PreAuthorize("@ss.hasPermi('members:help:add')")
    @Log(title = "党员帮扶", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberHelp djPartyMemberHelp)
    {
        return toAjax(djPartyMemberHelpService.insertDjPartyMemberHelp(djPartyMemberHelp));
    }

    /**
     * 修改党员帮扶
     */
    @PreAuthorize("@ss.hasPermi('members:help:edit')")
    @Log(title = "党员帮扶", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberHelp djPartyMemberHelp)
    {
        return toAjax(djPartyMemberHelpService.updateDjPartyMemberHelp(djPartyMemberHelp));
    }

    /**
     * 删除党员帮扶
     */
    @PreAuthorize("@ss.hasPermi('members:help:remove')")
    @Log(title = "党员帮扶", businessType = BusinessType.DELETE)
	@DeleteMapping("/{helpIds}")
    public AjaxResult remove(@PathVariable Long[] helpIds)
    {
        return toAjax(djPartyMemberHelpService.deleteDjPartyMemberHelpByIds(helpIds));
    }
}
