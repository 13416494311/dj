package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 党组织党费对象 dj_party_member_due_org
 *
 * @author ruoyi
 * @date 2021-06-15
 */
@Data
public class DjPartyMemberDueOrg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long dueOrgId;

    /** 党费计划UUID */
    @Excel(name = "党费计划UUID")
    private String duePlanUuid;

    private DjPartyMemberDuePlan duePlan;

    /** 党组织id */
    @Excel(name = "党组织id")
    private Long partyOrgId;

    private DjPartyOrg partyOrg;

    /** 状态(1：未提交  2：已提交) */
    @Excel(name = "状态(1：未提交  2：已提交)")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;



}
