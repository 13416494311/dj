package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织考核对象 dj_org_assessment
 * 
 * @author admin
 * @date 2021-03-10
 */
@Data
public class DjOrgAssessment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 考核uuid */
    @Excel(name = "考核uuid")
    private String assessmentUuid;

    /** assessmentYear_uuid
 */
    @Excel(name = "assessmentYear_uuid ")
    private String assessmentyearUuid;

    /** 党组织id */
    @Excel(name = "党组织id")
    private Long partyOrgId;
    private DjPartyOrg djPartyOrg;
    /** 考核年份 */
    @Excel(name = "考核年份")
    private String assessment_year;

    /** 考核名称 */
    @Excel(name = "考核名称")
    private String assessmentName;

    /** 考评等级结果 ，1：一级、2：二级、3：三级 */
    @Excel(name = "考评等级结果 ，1：一级、2：二级、3：三级")
    private String assessmentResult;

    /** 考核状态 0：党支部自评中、1：党委考核中、2考核完毕 */
    @Excel(name = "考核状态 0：党支部自评中、1：党委考核中、2考核完毕")
    private String orgAssessmentStatus;

    /** 排名 */
    @Excel(name = "排名")
    private Integer assessment_rank;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

}
