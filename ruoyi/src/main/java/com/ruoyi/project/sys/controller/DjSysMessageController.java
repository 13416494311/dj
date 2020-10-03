package com.ruoyi.project.sys.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * APP消息Controller
 *
 * @author ruoyi
 * @date 2020-10-03
 */
@RestController
@RequestMapping("/sys/message")
public class DjSysMessageController extends BaseController
{
    @Autowired
    private IDjSysMessageService djSysMessageService;

    /**
     * 查询APP消息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjSysMessage djSysMessage)
    {
        startPage();
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysMessage.setUserIds(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        }
        List<DjSysMessage> list = djSysMessageService.selectDjSysMessageList(djSysMessage);
        return getDataTable(list);
    }

    @RequestMapping("/listForApp")
    public AjaxResult listForApp(DjSysMessage djSysMessage)
    {
        startPage();
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysMessage.setUserIds(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        }
        List<DjSysMessage> list = djSysMessageService.selectDjSysMessageList(djSysMessage);
        return AjaxResult.success(list);
    }

    /**
     * 导出APP消息列表
     */
    @Log(title = "APP消息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjSysMessage djSysMessage)
    {
        List<DjSysMessage> list = djSysMessageService.selectDjSysMessageList(djSysMessage);
        ExcelUtil<DjSysMessage> util = new ExcelUtil<DjSysMessage>(DjSysMessage.class);
        return util.exportExcel(list, "message");
    }

    /**
     * 获取APP消息详细信息
     */
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return AjaxResult.success(djSysMessageService.selectDjSysMessageById(messageId));
    }

    /**
     * 新增APP消息
     */
    @Log(title = "APP消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjSysMessage djSysMessage)
    {
        return toAjax(djSysMessageService.insertDjSysMessage(djSysMessage));
    }

    /**
     * 修改APP消息
     */
    @Log(title = "APP消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjSysMessage djSysMessage)
    {
        return toAjax(djSysMessageService.updateDjSysMessage(djSysMessage));
    }

    /**
     * 删除APP消息
     */
    @Log(title = "APP消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(djSysMessageService.deleteDjSysMessageByIds(messageIds));
    }
}
