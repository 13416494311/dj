package com.ruoyi.project.party.controller;

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
import com.ruoyi.project.party.domain.DjPartyOrgPost;
import com.ruoyi.project.party.service.IDjPartyOrgPostService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织职务Controller
 *
 * @author ruoyi
 * @date 2020-08-08
 */
@RestController
@RequestMapping("/party/orgPost")
public class DjPartyOrgPostController extends BaseController
{
    @Autowired
    private IDjPartyOrgPostService djPartyOrgPostService;

    /**
     * 查询党组织职务列表
     */

    @GetMapping("/list")
    public TableDataInfo list(DjPartyOrgPost djPartyOrgPost)
    {
        startPage();
        List<DjPartyOrgPost> list = djPartyOrgPostService.selectDjPartyOrgPostList(djPartyOrgPost);
        return getDataTable(list);
    }

    /**
     * 导出党组织职务列表
     */

    @Log(title = "党组织职务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyOrgPost djPartyOrgPost)
    {
        List<DjPartyOrgPost> list = djPartyOrgPostService.selectDjPartyOrgPostList(djPartyOrgPost);
        ExcelUtil<DjPartyOrgPost> util = new ExcelUtil<DjPartyOrgPost>(DjPartyOrgPost.class);
        return util.exportExcel(list, "partyOrgPost");
    }

    /**
     * 获取党组织职务详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djPartyOrgPostService.selectDjPartyOrgPostById(id));
    }

    /**
     * 新增党组织职务
     */

    @Log(title = "党组织职务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyOrgPost djPartyOrgPost)
    {
        return toAjax(djPartyOrgPostService.insertDjPartyOrgPost(djPartyOrgPost));
    }

    /**
     * 修改党组织职务
     */

    @Log(title = "党组织职务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyOrgPost djPartyOrgPost)
    {
        return toAjax(djPartyOrgPostService.updateDjPartyOrgPost(djPartyOrgPost));
    }

    /**
     * 删除党组织职务
     */

    @Log(title = "党组织职务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djPartyOrgPostService.deleteDjPartyOrgPostByIds(ids));
    }
}
