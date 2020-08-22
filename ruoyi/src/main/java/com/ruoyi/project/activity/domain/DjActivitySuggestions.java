package com.ruoyi.project.activity.domain;


import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.system.domain.SysFile;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 建言献策对象 dj_activity_suggestions
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Data
public class DjActivitySuggestions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 建言献策ID */
    private Long suggestionsId;

    /** 建言献策uuid */
    @Excel(name = "建言献策uuid")
    private String suggestionsUuid;

    /** 活动ID */
    @Excel(name = "活动UUID")
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
