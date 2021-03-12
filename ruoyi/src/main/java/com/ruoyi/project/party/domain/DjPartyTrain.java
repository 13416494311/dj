package com.ruoyi.project.party.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织培训对象 dj_party_train
 *
 * @author ruoyi
 * @date 2021-03-09
 */
@Data
public class DjPartyTrain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long trainId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trainUuid;

    /** 组织机构 */
    @Excel(name = "组织机构")
    private Long partyOrgId;

    private DjPartyOrg partyOrg;

    /** 培训类别 */
    @Excel(name = "培训类别")
    private String trainType;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String activityTheme;

    /** 活动内容 */
    @Excel(name = "活动内容")
    private String activityContent;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activityStartTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activityEndTime;

    /** 有效学时 */
    @Excel(name = "有效学时")
    private Double effectiveHours;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
