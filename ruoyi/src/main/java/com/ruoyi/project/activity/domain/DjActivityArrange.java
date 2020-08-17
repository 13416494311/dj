package com.ruoyi.project.activity.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动安排对象 dj_activity_arrange
 *
 * @author ruoyi
 * @date 2020-08-15
 */
@Data
public class DjActivityArrange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 活动uuid */
    @Excel(name = "活动uuid")
    private String planUuid;

    private DjActivityPlan djActivityPlan;

    /** 活动党组织ID */
    @Excel(name = "活动党组织ID")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 负责人 */
    @Excel(name = "负责人")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String venue;

    /** 活动安排状态 */
    @Excel(name = "活动安排状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

}
