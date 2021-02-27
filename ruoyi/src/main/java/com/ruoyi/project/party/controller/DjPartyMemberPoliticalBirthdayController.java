package com.ruoyi.project.party.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ruoyi.project.party.domain.DjPartyMemberPoliticalBirthday;
import com.ruoyi.project.party.service.IDjPartyMemberPoliticalBirthdayService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 政治生日Controller
 *
 * @author ruoyi
 * @date 2021-02-21
 */
@RestController
@RequestMapping("/party/politicalBirthday")
public class DjPartyMemberPoliticalBirthdayController extends BaseController
{
    @Autowired
    private IDjPartyMemberPoliticalBirthdayService djPartyMemberPoliticalBirthdayService;

    /**
     * 查询政治生日列表
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o")
    public TableDataInfo list(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday,String memberName)
    {
        startPage();
        Map<String, Object> params = new HashMap<>();
        params.put("memberName",memberName);
        djPartyMemberPoliticalBirthday.setParams(params);
        List<DjPartyMemberPoliticalBirthday> list = djPartyMemberPoliticalBirthdayService.selectDjPartyMemberPoliticalBirthdayList(djPartyMemberPoliticalBirthday);
        return getDataTable(list);
    }

    /**
     * 导出政治生日列表
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:export')")
    @Log(title = "政治生日", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        List<DjPartyMemberPoliticalBirthday> list = djPartyMemberPoliticalBirthdayService.selectDjPartyMemberPoliticalBirthdayList(djPartyMemberPoliticalBirthday);
        ExcelUtil<DjPartyMemberPoliticalBirthday> util = new ExcelUtil<DjPartyMemberPoliticalBirthday>(DjPartyMemberPoliticalBirthday.class);
        return util.exportExcel(list, "politicalBirthday");
    }

    /**
     * 获取政治生日详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:query')")
    @GetMapping(value = "/{birthdayId}")
    public AjaxResult getInfo(@PathVariable("birthdayId") Long birthdayId)
    {
        return AjaxResult.success(djPartyMemberPoliticalBirthdayService.selectDjPartyMemberPoliticalBirthdayById(birthdayId));
    }

    /**
     * 新增政治生日
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:add')")
    @Log(title = "政治生日", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        return toAjax(djPartyMemberPoliticalBirthdayService.insertDjPartyMemberPoliticalBirthday(djPartyMemberPoliticalBirthday));
    }

    /**
     * 修改政治生日
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:edit')")
    @Log(title = "政治生日", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        return toAjax(djPartyMemberPoliticalBirthdayService.updateDjPartyMemberPoliticalBirthday(djPartyMemberPoliticalBirthday));
    }

    /**
     * 删除政治生日
     */
    @PreAuthorize("@ss.hasPermi('party:politicalBirthday:remove')")
    @Log(title = "政治生日", businessType = BusinessType.DELETE)
	@DeleteMapping("/{birthdayIds}")
    public AjaxResult remove(@PathVariable Long[] birthdayIds)
    {
        return toAjax(djPartyMemberPoliticalBirthdayService.deleteDjPartyMemberPoliticalBirthdayByIds(birthdayIds));
    }
}
