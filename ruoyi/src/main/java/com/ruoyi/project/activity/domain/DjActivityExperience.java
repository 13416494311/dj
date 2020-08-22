package com.ruoyi.project.activity.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.system.domain.SysFile;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 心得体会对象 dj_activity_experience
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@Data
public class DjActivityExperience extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 心得体会ID */
    private Long experienceId;

    /** 心得体会uuid */
    @Excel(name = "心得体会uuid")
    private String experienceUuid;

    /** 活动uuid */
    @Excel(name = "活动uuid")
    private String detailedUuid;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 党员id */
    @Excel(name = "党员id")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    private List<SysFile> sysFiles;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
