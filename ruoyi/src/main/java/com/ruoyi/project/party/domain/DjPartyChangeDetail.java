package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 换届职位详情对象 dj_party_change_detail
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@Data
public class DjPartyChangeDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long changeDetailId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String changeUuid;

    private Long partyOrgId;

    /** 换届职位 */
    @Excel(name = "换届职位")
    private String changeType;

    /** 换届前memberid */
    @Excel(name = "换届前memberid")
    private Long preMemberId;

    private DjPartyMember preMember;

    /** 换届后memberid */
    @Excel(name = "换届后memberid")
    private Long curMemberId;

    private DjPartyMember curMember;


}
