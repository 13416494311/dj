package com.ruoyi.project.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.ruoyi.framework.server.WebSocketServer;
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
import com.ruoyi.project.system.domain.SysComment;
import com.ruoyi.project.system.service.ISysCommentService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 点评Controller
 *
 * @author ruoyi
 * @date 2021-02-19
 */
@RestController
@RequestMapping("/system/comment")
public class SysCommentController extends BaseController
{
    @Autowired
    private ISysCommentService sysCommentService;
    @Autowired
    private WebSocketServer webSocketServer;
    /**
     * 查询点评列表
     */
    @PreAuthorize("@ss.hasPermi('system:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysComment sysComment)
    {
        startPage();
        List<SysComment> list = sysCommentService.selectSysCommentList(sysComment);
        return getDataTable(list);
    }

    /**
     * 查询点评列表
     */
    @GetMapping("/tree")
    public AjaxResult tree(SysComment sysComment)
    {
        Map<String,Object> map = new HashMap<>();
        map.put("commentList",sysCommentService.selectSysCommentTree(sysComment));
        map.put("commentNum",sysCommentService.selectSysCommentList(sysComment).size());
        return AjaxResult.success(map);
    }

    /**
     * 导出点评列表
     */
    @PreAuthorize("@ss.hasPermi('system:comment:export')")
    @Log(title = "点评", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysComment sysComment)
    {
        List<SysComment> list = sysCommentService.selectSysCommentList(sysComment);
        ExcelUtil<SysComment> util = new ExcelUtil<SysComment>(SysComment.class);
        return util.exportExcel(list, "comment");
    }

    /**
     * 获取点评详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:comment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysCommentService.selectSysCommentById(id));
    }

    /**
     * 新增点评
     */
    @Log(title = "点评", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysComment sysComment) throws Exception {
        ConcurrentHashMap<String, WebSocketServer> groupHashMap = webSocketServer.getWebSocketuGroup(sysComment.getBusinessUuid());
        if(groupHashMap!=null && groupHashMap.size() >0){
            webSocketServer.sendMessageToGroup(sysComment.getBusinessUuid(), JSON.toJSONString(sysComment));
        }
        return toAjax(sysCommentService.insertSysComment(sysComment));
    }




    /**
     * 修改点评
     */
    @PreAuthorize("@ss.hasPermi('system:comment:edit')")
    @Log(title = "点评", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysComment sysComment)
    {
        return toAjax(sysCommentService.updateSysComment(sysComment));
    }

    /**
     * 删除点评
     */
    @PreAuthorize("@ss.hasPermi('system:comment:remove')")
    @Log(title = "点评", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) throws Exception {
        for(Long id:ids){
            SysComment sysComment = sysCommentService.selectSysCommentById(id);
            ConcurrentHashMap<String, WebSocketServer> groupHashMap = webSocketServer.getWebSocketuGroup(sysComment.getBusinessUuid());
            if(groupHashMap!=null && groupHashMap.size() >0){
                webSocketServer.sendMessageToGroup(sysComment.getBusinessUuid(), JSON.toJSONString(sysComment));
            }
        }

        return toAjax(sysCommentService.deleteSysCommentByIds(ids));
    }
}
