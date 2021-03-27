package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织加减分审核对象 dj_org_assessment_addsubtract
 * 
 * @author admin
 * @date 2021-03-15
 */
@Data
public class DjOrgAssessmentAddsubtract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long addsubtractId;

    /** 加减分uuid */
    @Excel(name = "加减分uuid")
    private String addsubtractUuid;

    /** 考核uuid */
    @Excel(name = "考核uuid")
    private String assessmentUuid;
    private DjOrgAssessment djOrgAssessment;

    /** 党组织id */
    @Excel(name = "党组织id")
    private Long partyOrgId;
    private DjPartyOrg djPartyOrg;
    /** 类型 */
    @Excel(name = "类型")
    private String addsubtractType;

    /** 考核年份 */
    @Excel(name = "考核年份")
    private String addsubtractYear;

    /** 事项 */
    @Excel(name = "事项")
    private String addsubtractTheme;

    /** 描述 */
    @Excel(name = "描述")
    private String addsubtractContent;

    /** 分数 */
    @Excel(name = "分数")
    private Double addsubtractScore;

    /** 指标完成情况是否较差 0:否，1：是 */
    @Excel(name = "指标完成情况是否较差 0:否，1：是")
    private String completionEffects;

    /** 是否有重大不良影响 0:否，1：是 */
    @Excel(name = "是否有重大不良影响 0:否，1：是")
    private String adverseEffects;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

}
