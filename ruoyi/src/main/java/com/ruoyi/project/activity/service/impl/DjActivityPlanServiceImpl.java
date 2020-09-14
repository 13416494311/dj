package com.ruoyi.project.activity.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ruoyi.common.utils.DateIntervalUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.domain.DateInterval;
import com.ruoyi.project.activity.domain.DjActivityArrange;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.mapper.DjActivityDetailedMapper;
import com.ruoyi.project.activity.service.IDjActivityArrangeService;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityPlanMapper;
import com.ruoyi.project.activity.domain.DjActivityPlan;
import com.ruoyi.project.activity.service.IDjActivityPlanService;

/**
 * 活动计划Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-10
 */
@Service
public class DjActivityPlanServiceImpl implements IDjActivityPlanService {
    @Autowired
    private DjActivityPlanMapper djActivityPlanMapper;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    @Autowired
    private IDjActivityArrangeService djActivityArrangeService;
    @Autowired
    private ISysDictDataService dictDataService;
    /**
     * 查询活动计划
     *
     * @param planId 活动计划ID
     * @return 活动计划
     */
    @Override
    public DjActivityPlan selectDjActivityPlanById(Long planId) {
        return djActivityPlanMapper.selectDjActivityPlanById(planId);
    }

    @Override
    public DjActivityPlan selectDjActivityPlanByPlanUuid(String planUuid) {
        DjActivityPlan activityPlan = djActivityPlanMapper.selectDjActivityPlanByPlanUuid(planUuid);
        if(StringUtils.isNotNull(activityPlan.getActivityType())){
            activityPlan.setActivityTypeText(dictDataService.selectDictLabel("activity_type",activityPlan.getActivityType()));
        }
        return activityPlan;
    }

    /**
     * 查询活动计划列表
     *
     * @param djActivityPlan 活动计划
     * @return 活动计划
     */
    @Override
    public List<DjActivityPlan> selectDjActivityPlanList(DjActivityPlan djActivityPlan) {
        return djActivityPlanMapper.selectDjActivityPlanList(djActivityPlan);
    }

    /**
     * 新增活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    @Override
    public int insertDjActivityPlan(DjActivityPlan djActivityPlan) {
        djActivityPlan.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djActivityPlan.setCreateTime(DateUtils.getNowDate());
        createActivityDetailed(djActivityPlan);
        return djActivityPlanMapper.insertDjActivityPlan(djActivityPlan);
    }

    /**
     * 修改活动计划
     *
     * @param djActivityPlan 活动计划
     * @return 结果
     */
    @Override
    public int updateDjActivityPlan(DjActivityPlan djActivityPlan) {
        djActivityPlan.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityPlan.setUpdateTime(DateUtils.getNowDate());
        createActivityDetailed(djActivityPlan);
        return djActivityPlanMapper.updateDjActivityPlan(djActivityPlan);
    }


    private void createActivityDetailed(DjActivityPlan djActivityPlan){

        List<DateInterval> list = new ArrayList<DateInterval>();
        switch (djActivityPlan.getConveneCycle()) {
            case "2":
                list = DateIntervalUtil.getWeekDateInterval(
                        djActivityPlan.getCycleStartTime(),
                        djActivityPlan.getCycleEndTime());
                break;
            case "3":
                list = DateIntervalUtil.getMonthDateInterval(
                        Integer.parseInt(djActivityPlan.getCycleStartYear()),
                        Integer.parseInt(djActivityPlan.getCycleStartMonthUnit()),
                        Integer.parseInt(djActivityPlan.getCycleEndYear()),
                        Integer.parseInt(djActivityPlan.getCycleEndMonthUnit()));
                if(StringUtils.isNotEmpty(list)){
                    djActivityPlan.setCycleStartTime(list.get(0).getStartDate());
                    djActivityPlan.setCycleEndTime(list.get(list.size()-1).getEndDate());
                }
                break;
            case "7":
                list = DateIntervalUtil.getQuarterDateInterval(
                        Integer.parseInt(djActivityPlan.getCycleStartYear()),
                        Integer.parseInt(djActivityPlan.getCycleStartQuarterUnit()),
                        Integer.parseInt(djActivityPlan.getCycleEndYear()),
                        Integer.parseInt(djActivityPlan.getCycleEndQuarterUnit()));
                if(StringUtils.isNotEmpty(list)){
                    djActivityPlan.setCycleStartTime(list.get(0).getStartDate());
                    djActivityPlan.setCycleEndTime(list.get(list.size()-1).getEndDate());
                }
                break;
            case "8":
                list = DateIntervalUtil.getYearDateInterval(
                        Integer.parseInt(djActivityPlan.getCycleStartYear()),
                        Integer.parseInt(djActivityPlan.getCycleEndYear()));
                if(StringUtils.isNotEmpty(list)){
                    djActivityPlan.setCycleStartTime(list.get(0).getStartDate());
                    djActivityPlan.setCycleEndTime(list.get(list.size()-1).getEndDate());
                }
                break;
            default:
                list = null ;
                break;
        }

        if("2".equals(djActivityPlan.getStatus())){
            DjActivityArrange activityArrange = new DjActivityArrange();
            activityArrange.setPlanUuid(djActivityPlan.getPlanUuid());
            List<DjActivityArrange> arrangeList = djActivityArrangeService.selectDjActivityArrangeList(activityArrange);
            if(list!=null&&list.size()>0&&arrangeList!=null&&arrangeList.size()>0){
                for(int j=0;j<arrangeList.size();j++){
                    for(int i=0;i<list.size();i++){
                        DjActivityDetailed activityDetailed = new DjActivityDetailed();
                        activityDetailed.setPlanUuid(djActivityPlan.getPlanUuid());
                        activityDetailed.setPartyOrgId(arrangeList.get(j).getPartyOrgId());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(list.get(i).getStartDate());
                        switch (djActivityPlan.getConveneCycle()){
                            case "2":
                                activityDetailed.setCycle(String.valueOf(i+1));
                                break;
                            case "3":
                                activityDetailed.setCycle(
                                        String.valueOf(
                                                Integer.parseInt(djActivityPlan.getCycleStartMonthUnit())+i
                                        ));
                                break;
                            case "7":
                                activityDetailed.setCycle(
                                        String.valueOf(
                                                Integer.parseInt(djActivityPlan.getCycleStartQuarterUnit())+i
                                        ));
                                break;
                            case "8":
                                activityDetailed.setCycle(
                                        String.valueOf(
                                                Integer.parseInt(djActivityPlan.getCycleStartYear())+i
                                        ));
                                break;
                            default:
                                break;
                        }
                        activityDetailed.setPlanStartTime(list.get(i).getStartDate());
                        activityDetailed.setPlanEndTime(list.get(i).getEndDate());
                        djActivityDetailedService.insertDjActivityDetailed(activityDetailed);
                    }
                }

            }

        }

    }

    /**
     * 批量删除活动计划
     *
     * @param planIds 需要删除的活动计划ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityPlanByIds(Long[] planIds) {
        return djActivityPlanMapper.deleteDjActivityPlanByIds(planIds);
    }

    /**
     * 删除活动计划信息
     *
     * @param planId 活动计划ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityPlanById(Long planId) {
        return djActivityPlanMapper.deleteDjActivityPlanById(planId);
    }
}
