package com.ruoyi.project.members.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员帮扶对象 dj_party_member_help
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Data
public class DjPartyMemberHelp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long helpId;

    /** 帮助UUID */
    @Excel(name = "帮助UUID")
    private String helpUuid;

    /** 党员ID */
    @Excel(name = "党员ID")
    private Long partyMemberId;

    private DjPartyMember djPartyMember ;

    /** 党组织id */
    @Excel(name = "党组织id")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 组织认定 */
    @Excel(name = "组织认定")
    private String cognizance;

    /** 困难程度 */
    @Excel(name = "困难程度")
    private String economicSituation;

    /** 状态   1：处理中   2：已处理 */
    @Excel(name = "状态   1：处理中   2：已处理")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
