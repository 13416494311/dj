package com.ruoyi.project.activity.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
@Data
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

    private String activityTypeText;

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

    /** 周期截止年份 */
    @Excel(name = "周期截止年份")
    private String cycleEndYear;

    /** 周期开始年份(季度/月份) */
    @Excel(name = "周期开始年份月份")
    private String cycleStartMonthUnit;


    /** 周期截止年份(季度/月份) */
    @Excel(name = "周期截止年份月份")
    private String cycleEndMonthUnit;


    /** 周期开始年份(季度/月份) */
    @Excel(name = "周期开始年份季度")
    private String cycleStartQuarterUnit;


    /** 周期截止年份(季度/月份) */
    @Excel(name = "周期截止年份季度")
    private String cycleEndQuarterUnit;



    /** 活动计划要求 */
    @Excel(name = "活动计划要求")
    private String actPlanRequirements;

    /** 计划状态 */
    @Excel(name = "计划状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
