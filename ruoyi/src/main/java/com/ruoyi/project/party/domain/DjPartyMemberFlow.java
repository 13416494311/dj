package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员流动对象 dj_party_member_flow
 *
 * @author ruoyi
 * @date 2020-09-21
 */
@Data
public class DjPartyMemberFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流动表ID */
    private Long flowId;

    /** 流动党员ID */
    @Excel(name = "流动党员ID")
    private Long flowMemberId;

    private DjPartyMember flowMember;

    /** 流动类型(0:流入 1：流出) */
    @Excel(name = "流动类型(0:流入 1：流出)")
    private String flowType;

    /** 流动原因 */
    @Excel(name = "流动原因")
    private String flowReasons;

    /** 流动联系方式 */
    @Excel(name = "流动联系方式")
    private String flowContact;

    /** 流动党支部联系人 */
    @Excel(name = "流动党支部联系人")
    private String flowPartyBranchContactPeople;

    /** 流动党支部联系方式 */
    @Excel(name = "流动党支部联系方式")
    private String flowPartyBranchContact;

    /** 流出（入）地（单位） */
    @Excel(name = "流出", readConverterExp = "入=")
    private String flowCompany;

    /** 支部联系人 */
    @Excel(name = "支部联系人")
    private String flowCompanyPeople;

    /** 支部联系方式 */
    @Excel(name = "支部联系方式")
    private String flowCompanyContact;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
