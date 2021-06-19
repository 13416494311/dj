package com.ruoyi.project.party.domain;

import lombok.Data;
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
@Data
public class DjPartyMemberDue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long dueMemberId;

    /** 党费计划党组织关联id */
    @Excel(name = "党费计划党组织关联id")
    private Long dueOrgId;

    private DjPartyMemberDueOrg dueOrg;

    private DjPartyMemberDuePlan duePlan;

    private DjPartyOrg partyOrg;

    /** 党员ID */
    @Excel(name = "党员ID")
    private Long partyMemberId;

    private DjPartyMember partyMember;

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


}
