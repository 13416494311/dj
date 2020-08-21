package com.ruoyi.project.activity.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动参与人对象 dj_activity_member
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Data
public class DjActivityMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划参与人ID */
    private Long memberId;

    /** 活动计划ID */
    @Excel(name = "活动计划ID")
    private String planUuid;

    /** 党组织ID */
    @Excel(name = "党组织ID")
    private Long partyOrgId;

    /** 党组织成员id */
    @Excel(name = "党组织成员id")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    private String type;

    /** 活动参与人参与状态 */
    @Excel(name = "活动参与人参与状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
