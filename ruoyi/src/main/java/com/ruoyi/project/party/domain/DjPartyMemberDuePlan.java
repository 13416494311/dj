package com.ruoyi.project.party.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党费计划对象 dj_party_member_due_plan
 * 
 * @author wandengfeng
 * @date 2021-06-15
 */
public class DjPartyMemberDuePlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long duePlanId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String duePlanUuid;

    /** 年度 */
    @Excel(name = "年度")
    private String year;

    /** 季度 */
    @Excel(name = "季度")
    private Long quarter;

    /** 党费标题 */
    @Excel(name = "党费标题")
    private String title;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDuePlanId(Long duePlanId) 
    {
        this.duePlanId = duePlanId;
    }

    public Long getDuePlanId() 
    {
        return duePlanId;
    }
    public void setDuePlanUuid(String duePlanUuid) 
    {
        this.duePlanUuid = duePlanUuid;
    }

    public String getDuePlanUuid() 
    {
        return duePlanUuid;
    }
    public void setYear(String year) 
    {
        this.year = year;
    }

    public String getYear() 
    {
        return year;
    }
    public void setQuarter(Long quarter) 
    {
        this.quarter = quarter;
    }

    public Long getQuarter() 
    {
        return quarter;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
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
            .append("duePlanId", getDuePlanId())
            .append("duePlanUuid", getDuePlanUuid())
            .append("year", getYear())
            .append("quarter", getQuarter())
            .append("title", getTitle())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
