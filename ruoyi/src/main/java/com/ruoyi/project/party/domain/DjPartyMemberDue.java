package com.ruoyi.project.party.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员党费对象 dj_party_member_due
 * 
 * @author wandengfeng
 * @date 2021-06-15
 */
public class DjPartyMemberDue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long dueMemberId;

    /** 党费计划党组织关联id */
    @Excel(name = "党费计划党组织关联id")
    private Long dueOrgId;

    /** 党员ID */
    @Excel(name = "党员ID")
    private Long partyMemberId;

    /** 月份 */
    @Excel(name = "月份")
    private Long month;

    /** 工资 */
    @Excel(name = "工资")
    private Double salary;

    /** 比列 */
    @Excel(name = "比列")
    private String ratio;

    /** 党费 */
    @Excel(name = "党费")
    private Double due;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDueMemberId(Long dueMemberId) 
    {
        this.dueMemberId = dueMemberId;
    }

    public Long getDueMemberId() 
    {
        return dueMemberId;
    }
    public void setDueOrgId(Long dueOrgId) 
    {
        this.dueOrgId = dueOrgId;
    }

    public Long getDueOrgId() 
    {
        return dueOrgId;
    }
    public void setPartyMemberId(Long partyMemberId) 
    {
        this.partyMemberId = partyMemberId;
    }

    public Long getPartyMemberId() 
    {
        return partyMemberId;
    }
    public void setMonth(Long month) 
    {
        this.month = month;
    }

    public Long getMonth() 
    {
        return month;
    }
    public void setSalary(Double salary) 
    {
        this.salary = salary;
    }

    public Double getSalary() 
    {
        return salary;
    }
    public void setRatio(String ratio) 
    {
        this.ratio = ratio;
    }

    public String getRatio() 
    {
        return ratio;
    }
    public void setDue(Double due) 
    {
        this.due = due;
    }

    public Double getDue() 
    {
        return due;
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
            .append("dueMemberId", getDueMemberId())
            .append("dueOrgId", getDueOrgId())
            .append("partyMemberId", getPartyMemberId())
            .append("month", getMonth())
            .append("salary", getSalary())
            .append("ratio", getRatio())
            .append("due", getDue())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
