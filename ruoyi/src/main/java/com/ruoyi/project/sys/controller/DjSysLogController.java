package com.ruoyi.project.sys.controller;

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
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.service.IDjSysLogService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 操作日志Controller
 *
 * @author ruoyi
 * @date 2020-09-18
 */
@RestController
@RequestMapping("/sys/log")
public class DjSysLogController extends BaseController
{
    @Autowired
    private IDjSysLogService djSysLogService;

    /**
     * 查询操作日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjSysLog djSysLog)
    {
        startPage();
        List<DjSysLog> list = djSysLogService.selectDjSysLogList(djSysLog);
        return getDataTable(list);
    }

    /**
     * 导出操作日志列表
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjSysLog djSysLog)
    {
        List<DjSysLog> list = djSysLogService.selectDjSysLogList(djSysLog);
        ExcelUtil<DjSysLog> util = new ExcelUtil<DjSysLog>(DjSysLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 获取操作日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djSysLogService.selectDjSysLogById(id));
    }

    /**
     * 新增操作日志
     */
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjSysLog djSysLog)
    {
        return toAjax(djSysLogService.insertDjSysLog(djSysLog));
    }

    /**
     * 修改操作日志
     */
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjSysLog djSysLog)
    {
        return toAjax(djSysLogService.updateDjSysLog(djSysLog));
    }

    /**
     * 删除操作日志
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djSysLogService.deleteDjSysLogByIds(ids));
    }
}
