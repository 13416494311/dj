package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织述职评议对象 dj_org_assessment_assessor
 *
 * @author admin
 * @date 2021-03-23
 */
@Data
public class DjOrgAssessmentAssessor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 考核uuid
     */
    @Excel(name = "考核uuid")
    private String assessmentUuid;

    /**
     * 评审人
     */
    @Excel(name = "评审人")
    private Long assessorId;

    /**
     * 评审人党组织id
     */
    @Excel(name = "评审人党组织id")
    private Long partyOrgId;
    private DjPartyMember djPartyMember ;
    private DjPartyOrg djPartyOrg;
    /**
     * 评审员类型
     */
    @Excel(name = "评审员类型")
    private String assessorType;

    /**
     * 表现程度
     */
    @Excel(name = "表现程度")
    private String evaluationContent1;

    /**
     * 表现程度
     */
    @Excel(name = "表现程度")
    private String evaluationContent2;

    /**
     * 表现程度
     */
    @Excel(name = "表现程度")
    private String evaluationContent3;

    /**
     * 表现程度
     */
    @Excel(name = "表现程度")
    private String evaluationContent4;

    /**
     * 表现程度
     */
    @Excel(name = "表现程度")
    private String evaluationContent5;

    /**
     * 综合评分
     */
    @Excel(name = "综合评分")
    private Double comprehensiveScore;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}
