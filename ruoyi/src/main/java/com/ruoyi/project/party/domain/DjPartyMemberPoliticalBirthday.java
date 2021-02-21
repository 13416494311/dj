package com.ruoyi.project.party.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 政治生日对象 dj_party_member_political_birthday
 *
 * @author ruoyi
 * @date 2021-02-21
 */
@Data
public class DjPartyMemberPoliticalBirthday extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long birthdayId;

    /** 唯一id */
    @Excel(name = "唯一id")
    private String birthdayUuid;

    /** 党员ID */
    @Excel(name = "党员ID")
    private Long memberId;

    private DjPartyMember partyMember;

    /** 组织机构id */
    @Excel(name = "组织机构id")
    private Long partyOrgId;

    private DjPartyOrg partyOrg;

    /** 政治生日日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "政治生日日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date politicalBirthday;

    /** 党龄 */
    @Excel(name = "党龄")
    private Long politicalAge;

    /** 发布日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 主题 */
    @Excel(name = "主题")
    private String theme;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
