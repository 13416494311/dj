package com.ruoyi.project.members.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员建议对象 dj_party_member_suggestions
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Data
public class DjPartyMemberSuggestions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 建议id */
    private Long suggestionsId;

    /** 建议uuid */
    @Excel(name = "建议uuid")
    private String suggestionsUuid;

    private String type ;

    /** 党员ID */
    @Excel(name = "党员ID")
    private Long memberId;

    private DjPartyMember partyMember;
    /** 党组织id */
    @Excel(name = "党组织id")
    private Long partyOrgId;

    private DjPartyOrg partyOrg;

    /** 建议内容 */
    @Excel(name = "建议内容")
    private String suggestionsContent;

    /** 填写日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "填写日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordTime;

    /** 处理情况 */
    @Excel(name = "处理情况")
    private String handleContent;

    /** 处理党员Id */
    @Excel(name = "处理党员Id")
    private Long handleMemberId;

    private DjPartyMember handleMember;

    /** 处理日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 是否已处理 Y 是 N 否 */
    @Excel(name = "是否已处理 Y 是 N 否")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
