package com.ruoyi.project.activity.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.activity.domain.DjActivityArrange;
import com.ruoyi.project.activity.service.IDjActivityArrangeService;
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
import com.ruoyi.project.activity.domain.DjActivityPlan;
import com.ruoyi.project.activity.service.IDjActivityPlanService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动计划Controller
 *
 * @author ruoyi
 * @date 2020-08-10
 */
@RestController
@RequestMapping("/activity/plan")
public class DjActivityPlanController extends BaseController
{
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjActivityArrangeService djActivityArrangeService;
    /**
     * 查询活动计划列表
     */
    @GetMapping("/list")
    @DataScope(partyOrgAlias = "o")
    public TableDataInfo list(DjActivityPlan djActivityPlan)
    {
        startPage();
        List<DjActivityPlan> list = djActivityPlanService.selectDjActivityPlanList(djActivityPlan);
        return getDataTable(list);
    }

    /**
     * 导出活动计划列表
     */
    @Log(title = "活动计划", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityPlan djActivityPlan)
    {
        List<DjActivityPlan> list = djActivityPlanService.selectDjActivityPlanList(djActivityPlan);
        ExcelUtil<DjActivityPlan> util = new ExcelUtil<DjActivityPlan>(DjActivityPlan.class);
        return util.exportExcel(list, "plan");
    }

    /**
     * 获取活动计划详细信息
     */
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId)
    {
        return AjaxResult.success(djActivityPlanService.selectDjActivityPlanById(planId));
    }

    /**
     * 新增活动计划
     */
    @Log(title = "活动计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityPlan djActivityPlan)
    {
        DjActivityArrange activityArrange = new DjActivityArrange();
        activityArrange.setPlanUuid(djActivityPlan.getPlanUuid());
        activityArrange.setDelFlag("0");
        List<DjActivityArrange> activityArrangeList = djActivityArrangeService.selectDjActivityArrangeList(activityArrange);
        AtomicReference<Boolean> haveOrgLeader = new AtomicReference<>(true);
        final String[] result = {""};
        activityArrangeList.stream().forEach(djActivityArrange->{

            if(djActivityArrange.getDjPartyOrg().getLeader()==null || djActivityArrange.getDjPartyOrg().getLeader()==0){
                haveOrgLeader.set(false);
                result[0] +=  djActivityArrange.getDjPartyOrg().getPartyOrgName() +"  未设置活动负责人；";
            }
        });

        if(!haveOrgLeader.get()){
            return AjaxResult.error( result[0]);
        }else{
            return toAjax(djActivityPlanService.insertDjActivityPlan(djActivityPlan));
        }

    }

    /**
     * 修改活动计划
     */
    @Log(title = "活动计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityPlan djActivityPlan)
    {
        return toAjax(djActivityPlanService.updateDjActivityPlan(djActivityPlan));
    }

    /**
     * 删除活动计划
     */
    @Log(title = "活动计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds)
    {
        return toAjax(djActivityPlanService.deleteDjActivityPlanByIds(planIds));
    }
}
