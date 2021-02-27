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
import com.ruoyi.project.members.domain.DjPartyMemberHelpRecord;
import com.ruoyi.project.members.service.IDjPartyMemberHelpRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 帮扶记录Controller
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
@RestController
@RequestMapping("/members/helpRecord")
public class DjPartyMemberHelpRecordController extends BaseController
{
    @Autowired
    private IDjPartyMemberHelpRecordService djPartyMemberHelpRecordService;

    /**
     * 查询帮扶记录列表
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        startPage();
        List<DjPartyMemberHelpRecord> list = djPartyMemberHelpRecordService.selectDjPartyMemberHelpRecordList(djPartyMemberHelpRecord);
        return getDataTable(list);
    }

    /**
     * 导出帮扶记录列表
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:export')")
    @Log(title = "帮扶记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        List<DjPartyMemberHelpRecord> list = djPartyMemberHelpRecordService.selectDjPartyMemberHelpRecordList(djPartyMemberHelpRecord);
        ExcelUtil<DjPartyMemberHelpRecord> util = new ExcelUtil<DjPartyMemberHelpRecord>(DjPartyMemberHelpRecord.class);
        return util.exportExcel(list, "helpRecord");
    }

    /**
     * 获取帮扶记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:query')")
    @GetMapping(value = "/{helpRecordId}")
    public AjaxResult getInfo(@PathVariable("helpRecordId") Long helpRecordId)
    {
        return AjaxResult.success(djPartyMemberHelpRecordService.selectDjPartyMemberHelpRecordById(helpRecordId));
    }

    /**
     * 新增帮扶记录
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:add')")
    @Log(title = "帮扶记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        return toAjax(djPartyMemberHelpRecordService.insertDjPartyMemberHelpRecord(djPartyMemberHelpRecord));
    }

    /**
     * 修改帮扶记录
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:edit')")
    @Log(title = "帮扶记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberHelpRecord djPartyMemberHelpRecord)
    {
        return toAjax(djPartyMemberHelpRecordService.updateDjPartyMemberHelpRecord(djPartyMemberHelpRecord));
    }

    /**
     * 删除帮扶记录
     */
    @PreAuthorize("@ss.hasPermi('members:helpRecord:remove')")
    @Log(title = "帮扶记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{helpRecordIds}")
    public AjaxResult remove(@PathVariable Long[] helpRecordIds)
    {
        return toAjax(djPartyMemberHelpRecordService.deleteDjPartyMemberHelpRecordByIds(helpRecordIds));
    }
}
