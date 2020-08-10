package com.ruoyi.project.party.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织职务对象 dj_party_org_post
 *
 * @author ruoyi
 * @date 2020-08-08
 */
@Data
public class DjPartyOrgPost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 党组织uuid */
    private String partyOrgUuid;

    /** 党员 */
    @Excel(name = "党员")
    private Long partyMemberId;

    private DjPartyMember djPartyMember;

    /** 职务 */
    @Excel(name = "职务")
    private String partyPositionType;

    /** 任职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tenureTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
