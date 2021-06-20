package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 绩效考核评分对象 dj_org_assessment_performance_score
 *
 * @author ruoyi
 * @date 2021-06-20
 */
@Data
public class DjOrgAssessmentPerformanceScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** assessment_uuid */
    @Excel(name = "assessment_uuid")
    private String assessmentUuid;

    /** 考核项目 */
    @Excel(name = "考核项目")
    private String item;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 分数 */
    @Excel(name = "分数")
    private Double score;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
