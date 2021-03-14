package com.ruoyi.project.party.controller;

import java.util.List;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
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
import com.ruoyi.project.party.domain.DjPartyTrain;
import com.ruoyi.project.party.service.IDjPartyTrainService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织培训Controller
 *
 * @author ruoyi
 * @date 2021-03-09
 */
@RestController
@RequestMapping("/party/train")
public class DjPartyTrainController extends BaseController
{
    @Autowired
    private IDjPartyTrainService djPartyTrainService;

    /**
     * 查询党组织培训列表
     */
    @PreAuthorize("@ss.hasPermi('party:train:list')")
    @GetMapping("/list")
    @DataScope( partyOrgAlias = "m", userAlias = "u" )
    public TableDataInfo list(DjPartyTrain djPartyTrain)
    {
        startPage();
        List<DjPartyTrain> list = djPartyTrainService.selectDjPartyTrainList(djPartyTrain);
        return getDataTable(list);
    }

    /**
     * 导出党组织培训列表
     */
    @PreAuthorize("@ss.hasPermi('party:train:export')")
    @Log(title = "党组织培训", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyTrain djPartyTrain)
    {
        List<DjPartyTrain> list = djPartyTrainService.selectDjPartyTrainList(djPartyTrain);
        ExcelUtil<DjPartyTrain> util = new ExcelUtil<DjPartyTrain>(DjPartyTrain.class);
        return util.exportExcel(list, "train");
    }

    /**
     * 获取党组织培训详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:train:query')")
    @GetMapping(value = "/{trainId}")
    public AjaxResult getInfo(@PathVariable("trainId") Long trainId)
    {
        return AjaxResult.success(djPartyTrainService.selectDjPartyTrainById(trainId));
    }

    /**
     * 新增党组织培训
     */
    @PreAuthorize("@ss.hasPermi('party:train:add')")
    @Log(title = "党组织培训", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyTrain djPartyTrain)
    {
        return toAjax(djPartyTrainService.insertDjPartyTrain(djPartyTrain));
    }

    /**
     * 修改党组织培训
     */
    @PreAuthorize("@ss.hasPermi('party:train:edit')")
    @Log(title = "党组织培训", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyTrain djPartyTrain)
    {
        return toAjax(djPartyTrainService.updateDjPartyTrain(djPartyTrain));
    }

    /**
     * 删除党组织培训
     */
    @PreAuthorize("@ss.hasPermi('party:train:remove')")
    @Log(title = "党组织培训", businessType = BusinessType.DELETE)
	@DeleteMapping("/{trainIds}")
    public AjaxResult remove(@PathVariable Long[] trainIds)
    {
        return toAjax(djPartyTrainService.deleteDjPartyTrainByIds(trainIds));
    }
}
