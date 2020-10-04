package com.ruoyi.project.sys.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
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
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 待办Controller
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@RestController
@RequestMapping("/sys/todo")
public class DjSysTodoController extends BaseController
{
    @Autowired
    private IDjSysTodoService djSysTodoService;

    @GetMapping(value = "/getTodoCount/{status}")
    public AjaxResult getTodoCount(@PathVariable("status") String status)
    {
        return AjaxResult.success(djSysTodoService.getTodoCount(status));
    }


    /**
     * 查询待办列表
     */
    @RequestMapping("/list")
    public TableDataInfo list(DjSysTodo djSysTodo)
    {
        startPage();
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysTodo.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        }
        List<DjSysTodo> list = djSysTodoService.selectDjSysTodoList(djSysTodo);
        return getDataTable(list);
    }

    @RequestMapping("/listForApp")
    public AjaxResult listForApp(@RequestBody DjSysTodo djSysTodo)
    {
        startPage();
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysTodo.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        }
        List<DjSysTodo> list = djSysTodoService.selectDjSysTodoList(djSysTodo);
        return AjaxResult.success(list);
    }

    /**
     * 导出待办列表
     */
    @Log(title = "待办", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjSysTodo djSysTodo)
    {
        List<DjSysTodo> list = djSysTodoService.selectDjSysTodoList(djSysTodo);
        ExcelUtil<DjSysTodo> util = new ExcelUtil<DjSysTodo>(DjSysTodo.class);
        return util.exportExcel(list, "todo");
    }

    /**
     * 获取待办详细信息
     */
    @GetMapping(value = "/{todoId}")
    public AjaxResult getInfo(@PathVariable("todoId") Long todoId)
    {
        return AjaxResult.success(djSysTodoService.selectDjSysTodoById(todoId));
    }

    /**
     * 新增待办
     */
    @Log(title = "待办", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjSysTodo djSysTodo)
    {
        return toAjax(djSysTodoService.insertDjSysTodo(djSysTodo));
    }

    /**
     * 修改待办
     */
    @Log(title = "待办", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjSysTodo djSysTodo)
    {
        return toAjax(djSysTodoService.updateDjSysTodo(djSysTodo));
    }

    /**
     * 删除待办
     */
    @Log(title = "待办", businessType = BusinessType.DELETE)
	@DeleteMapping("/{todoIds}")
    public AjaxResult remove(@PathVariable Long[] todoIds)
    {
        return toAjax(djSysTodoService.deleteDjSysTodoByIds(todoIds));
    }
}
