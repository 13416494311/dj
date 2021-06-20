package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织考核年对象 dj_org_assessmentyear
 *
 * @author admin
 * @date 2021-03-10
 */
@Data
public class DjOrgAssessmentyear extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * UUID
     */
    @Excel(name = "UUID")
    private String assessmentyearUuid;

    /**
     * 考核年份
     */
    @Excel(name = "考核年份")
    private String year;

    /**
     * 考核名称
     */
    @Excel(name = "考核名称")
    private String assessmentName;

    /**
     * 考核状态（0：考核中、1：考核结束）
     */
    @Excel(name = "考核状态", readConverterExp = "0=：考核中、1：考核结束")
    private String orgAssessmentStatus;

    /** 绩效考评状态 */
    private String performanceAppraisalStatus;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


}
