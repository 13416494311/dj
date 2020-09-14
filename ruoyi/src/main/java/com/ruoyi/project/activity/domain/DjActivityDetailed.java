package com.ruoyi.project.activity.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动详情对象 dj_activity_detailed
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Data
public class DjActivityDetailed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long detailedId;

    private String detailedUuid;

    /** 计划uuid */
    @Excel(name = "计划uuid")
    private String planUuid;

    private DjActivityPlan djActivityPlan;

    /** 活动党组织ID */
    @Excel(name = "活动党组织ID")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 周期 */
    @Excel(name = "周期")
    private String cycle;

    /** 负责人 */
    @Excel(name = "负责人")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String venue;

    /** 活动内容 */
    @Excel(name = "活动内容")
    private String activityContent;

    /** 计划召开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划召开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndTime;

    /** 活动计划召开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "活动计划召开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activityPlanStartTime;

    /** 活动计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "活动计划结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activityPlanEndTime;

    /** 活动实际开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "活动实际开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualStartTime;

    /** 活动实际结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "活动实际结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualEndTime;

    /** 记录人 */
    @Excel(name = "记录人")
    private String recorder;

    /** 到会指导人员 */
    @Excel(name = "到会指导人员")
    private String mentors;

    /** 主持人 */
    @Excel(name = "主持人")
    private String presenter;

    /** 主讲人及身份 */
    @Excel(name = "主讲人及身份")
    private String speaker;

    /** 是否允许评论消息 */
    @Excel(name = "是否允许评论消息")
    private String allowComment;

    /** 活动投票ID */
    @Excel(name = "活动投票ID")
    private Long activityVoteId;

    /** 活动考试ID */
    @Excel(name = "活动考试ID")
    private Long activityExamId;

    /** 活动状态 */
    @Excel(name = "活动状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private List<DjActivityMember> djActivityMemberList;


}
