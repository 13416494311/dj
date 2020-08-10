package com.ruoyi.project.activity.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动计划对象 dj_activity_plan
 *
 * @author ruoyi
 * @date 2020-08-10
 */
public class DjActivityPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动计划ID */
    private Long planId;

    /** 活动唯一uuid */
    @Excel(name = "活动唯一uuid")
    private String planUuid;

    /** 活动来源 */
    @Excel(name = "活动来源")
    private String activitySources;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String activityTheme;

    /** 党员活动类型 */
    @Excel(name = "党员活动类型")
    private String activityType;

    /** 召开周期 */
    @Excel(name = "召开周期")
    private String conveneCycle;

    /** 周期开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "周期开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cycleStartTime;

    /** 周期结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "周期结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cycleEndTime;

    /** 周期开始年份 */
    @Excel(name = "周期开始年份")
    private String cycleStartYear;

    /** 周期开始年份(季度/月份) */
    @Excel(name = "周期开始年份(季度/月份)")
    private String cycleStartYearUnit;

    /** 周期截止年份 */
    @Excel(name = "周期截止年份")
    private String cycleEndYear;

    /** 周期截止年份(季度/月份) */
    @Excel(name = "周期截止年份(季度/月份)")
    private String cycleEndYearUnit;

    /** 活动计划要求 */
    @Excel(name = "活动计划要求")
    private String actPlanRequirements;

    /** 计划状态 */
    @Excel(name = "计划状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public Long getPlanId()
    {
        return planId;
    }
    public void setPlanUuid(String planUuid)
    {
        this.planUuid = planUuid;
    }

    public String getPlanUuid()
    {
        return planUuid;
    }
    public void setActivitySources(String activitySources)
    {
        this.activitySources = activitySources;
    }

    public String getActivitySources()
    {
        return activitySources;
    }
    public void setActivityTheme(String activityTheme)
    {
        this.activityTheme = activityTheme;
    }

    public String getActivityTheme()
    {
        return activityTheme;
    }
    public void setActivityType(String activityType)
    {
        this.activityType = activityType;
    }

    public String getActivityType()
    {
        return activityType;
    }
    public void setConveneCycle(String conveneCycle)
    {
        this.conveneCycle = conveneCycle;
    }

    public String getConveneCycle()
    {
        return conveneCycle;
    }
    public void setCycleStartTime(Date cycleStartTime)
    {
        this.cycleStartTime = cycleStartTime;
    }

    public Date getCycleStartTime()
    {
        return cycleStartTime;
    }
    public void setCycleEndTime(Date cycleEndTime)
    {
        this.cycleEndTime = cycleEndTime;
    }

    public Date getCycleEndTime()
    {
        return cycleEndTime;
    }
    public void setCycleStartYear(String cycleStartYear)
    {
        this.cycleStartYear = cycleStartYear;
    }

    public String getCycleStartYear()
    {
        return cycleStartYear;
    }
    public void setCycleStartYearUnit(String cycleStartYearUnit)
    {
        this.cycleStartYearUnit = cycleStartYearUnit;
    }

    public String getCycleStartYearUnit()
    {
        return cycleStartYearUnit;
    }
    public void setCycleEndYear(String cycleEndYear)
    {
        this.cycleEndYear = cycleEndYear;
    }

    public String getCycleEndYear()
    {
        return cycleEndYear;
    }
    public void setCycleEndYearUnit(String cycleEndYearUnit)
    {
        this.cycleEndYearUnit = cycleEndYearUnit;
    }

    public String getCycleEndYearUnit()
    {
        return cycleEndYearUnit;
    }
    public void setActPlanRequirements(String actPlanRequirements)
    {
        this.actPlanRequirements = actPlanRequirements;
    }

    public String getActPlanRequirements()
    {
        return actPlanRequirements;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("planId", getPlanId())
            .append("planUuid", getPlanUuid())
            .append("activitySources", getActivitySources())
            .append("activityTheme", getActivityTheme())
            .append("activityType", getActivityType())
            .append("conveneCycle", getConveneCycle())
            .append("cycleStartTime", getCycleStartTime())
            .append("cycleEndTime", getCycleEndTime())
            .append("cycleStartYear", getCycleStartYear())
            .append("cycleStartYearUnit", getCycleStartYearUnit())
            .append("cycleEndYear", getCycleEndYear())
            .append("cycleEndYearUnit", getCycleEndYearUnit())
            .append("actPlanRequirements", getActPlanRequirements())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
