package com.ruoyi.project.party.controller;

import java.util.List;
import java.util.UUID;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.party.domain.DjOrgAssessmentList;
import com.ruoyi.project.party.domain.DjOrgAssessmentListScore;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;
import com.ruoyi.project.party.service.IDjOrgAssessmentListScoreService;
import com.ruoyi.project.party.service.IDjOrgAssessmentListService;
import com.ruoyi.project.party.service.IDjOrgAssessmentyearService;
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
import com.ruoyi.project.party.domain.DjOrgAssessment;
import com.ruoyi.project.party.service.IDjOrgAssessmentService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织考核Controller
 * 
 * @author admin
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/party/assessment")
public class DjOrgAssessmentController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentService djOrgAssessmentService;
    @Autowired
    private IDjOrgAssessmentyearService djOrgAssessmentyearService;
    @Autowired
    private IDjOrgAssessmentListService djOrgAssessmentListService;
    @Autowired
    private IDjOrgAssessmentListScoreService djOrgAssessmentListScoreService;

    /**
     * 查询党组织考核列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:list')")
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o")
    public TableDataInfo list(DjOrgAssessment djOrgAssessment)
    {
        startPage();
        List<DjOrgAssessment> list = djOrgAssessmentService.selectDjOrgAssessmentList(djOrgAssessment);
        return getDataTable(list);
    }

    /**
     * 导出党组织考核列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:export')")
    @Log(title = "党组织考核", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessment djOrgAssessment)
    {
        List<DjOrgAssessment> list = djOrgAssessmentService.selectDjOrgAssessmentList(djOrgAssessment);
        ExcelUtil<DjOrgAssessment> util = new ExcelUtil<DjOrgAssessment>(DjOrgAssessment.class);
        return util.exportExcel(list, "assessment");
    }

    /**
     * 获取党组织考核详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentService.selectDjOrgAssessmentById(id));
    }

    /**
     * 新增党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:add')")
    @Log(title = "党组织考核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        return toAjax(djOrgAssessmentService.insertDjOrgAssessment(djOrgAssessment));
    }

    /**
     * 新增活党组织考核
     */
    @Log(title = "党组织考核", businessType = BusinessType.INSERT)
    @PostMapping("/addByOrg")
    public AjaxResult addByOrg(String assessmentyearUuid,String partyOrgIds,String year,String assessmentName)
    {
        DjOrgAssessmentList djOrgAssessmentList = new DjOrgAssessmentList();
        djOrgAssessmentList.setStatus("0");
        List<DjOrgAssessmentList> djOrgAssessmentListAll = djOrgAssessmentListService.selectDjOrgAssessmentListList(djOrgAssessmentList);
        for(String partyOrgId:partyOrgIds.split(",")){
            DjOrgAssessmentListScore djOrgAssessmentListScore = new DjOrgAssessmentListScore();
            String uuid = UUID.randomUUID().toString();
            DjOrgAssessment djOrgAssessment =new DjOrgAssessment();
            djOrgAssessment.setAssessmentUuid(uuid);
            djOrgAssessment.setAssessmentyearUuid(assessmentyearUuid);
            djOrgAssessment.setPartyOrgId(Long.parseLong(partyOrgId));
            djOrgAssessment.setAssessment_year(year);
            djOrgAssessment.setAssessmentName(assessmentName);
            djOrgAssessment.setOrgAssessmentStatus("1");
            djOrgAssessmentService.insertDjOrgAssessment(djOrgAssessment);
            for (DjOrgAssessmentList djOrgAssessmentListOne:djOrgAssessmentListAll) {
                djOrgAssessmentListScore.setAssessmentUuid(uuid);
                djOrgAssessmentListScore.setItem(djOrgAssessmentListOne.getItem());
                djOrgAssessmentListScore.setContent(djOrgAssessmentListOne.getContent());
                djOrgAssessmentListScore.setQuota(djOrgAssessmentListOne.getQuota());
                djOrgAssessmentListScore.setScore(djOrgAssessmentListOne.getScore());
                djOrgAssessmentListScore.setCriteria(djOrgAssessmentListOne.getCriteria());
                djOrgAssessmentListScore.setOrderNum(djOrgAssessmentListOne.getOrderNum());
                djOrgAssessmentListScore.setSelfScore(0.0);
                djOrgAssessmentListScore.setDelFlag("0");
                djOrgAssessmentListScore.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
                djOrgAssessmentListScore.setCreateTime(DateUtils.getNowDate());
                djOrgAssessmentListScoreService.insertDjOrgAssessmentListScore(djOrgAssessmentListScore);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 修改党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:edit')")
    @Log(title = "党组织考核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        return toAjax(djOrgAssessmentService.updateDjOrgAssessment(djOrgAssessment));
    }

    /**
     * 删除党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:remove')")
    @Log(title = "党组织考核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentService.deleteDjOrgAssessmentByIds(ids));
    }
}
