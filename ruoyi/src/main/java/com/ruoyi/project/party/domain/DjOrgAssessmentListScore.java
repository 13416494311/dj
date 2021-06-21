package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 考核评价评分对象 dj_org_assessment_list_score
 *
 * @author admin
 * @date 2021-03-12
 */
@Data
public class DjOrgAssessmentListScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** assessment_uuid */
    @Excel(name = "assessment_uuid")
    private String assessmentUuid;

    private String type;

    /** 考核项目 */
    @Excel(name = "考核项目")
    private String item;

    /** 考核内容 */
    @Excel(name = "考核内容")
    private String content;

    /** 考核指标 */
    @Excel(name = "考核指标")
    private String quota;

    /** 分值 */
    @Excel(name = "分值")
    private Integer score;

    /** 评分标准 */
    @Excel(name = "评分标准")
    private String criteria;

    /** 自评分数 */
    @Excel(name = "自评分数")
    private Double selfScore;

    /** 党委评分 */
    @Excel(name = "党委评分")
    private Double assessorScore;

    @Excel(name = "绩效评分")
    private Double performanceAppraisalScore;


    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
