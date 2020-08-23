package com.ruoyi.project.activity.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动督办对象 dj_activity_supervise
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@Data
public class DjActivitySupervise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 督办ID */
    private Long superviseId;

    /** 活动uuid */
    @Excel(name = "活动uuid")
    private String detailedUuid;

    /** 督办内容 */
    @Excel(name = "督办内容")
    private String content;

    /** 督办党员id */
    @Excel(name = "督办党员id")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
