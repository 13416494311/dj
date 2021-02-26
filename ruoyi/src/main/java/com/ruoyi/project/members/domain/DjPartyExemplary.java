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
 * 先锋模范对象 dj_party_exemplary
 *
 * @author ruoyi
 * @date 2021-02-26
 */
@Data
public class DjPartyExemplary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID
 */
    private Long exemplaryId;

    /** 模范UUID */
    @Excel(name = "模范UUID")
    private String exemplaryUuid;

    /** 用户ID
 */
    @Excel(name = "用户ID")
    private Long memberId;

    private DjPartyMember djPartyMember ;
    /** 党组织id
 */
    @Excel(name = "党组织id")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 记录日期
 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordTime;

    /** 模范事项
 */
    @Excel(name = "模范事项")
    private String exemplaryTheme;

    /** 模范描述
 */
    @Excel(name = "模范描述")
    private String helpRecord;

    /** 状态 */
    @Excel(name = "状态")
    private String auditState;

    /** 删除标志（0代表存在 2代表删除）
 */
    private String delFlag;


}
