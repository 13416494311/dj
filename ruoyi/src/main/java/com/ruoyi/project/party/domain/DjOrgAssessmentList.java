package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织考核评价清单对象 dj_org_assessment_list
 *
 * @author admin
 * @date 2021-03-08
 */
@Data
public class DjOrgAssessmentList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long listId;

    /**
     * 考核项目
     */
    @Excel(name = "考核项目")
    private String item;

    /**
     * 考核内容
     */
    @Excel(name = "考核内容")
    private String content;

    /**
     * 考核指标
     */
    @Excel(name = "考核指标")
    private String quota;

    /**
     * 分值
     */
    @Excel(name = "分值")
    private Integer score;

    /**
     * 评分标准
     * 评分标准
     */
    @Excel(name = "评分标准 评分标准")
    private String criteria;

    /**
     * 是否启用（1：是 0：否）
     */
    @Excel(name = "是否启用", readConverterExp = "1=：是,0=：否")
    private String status;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long orderNum;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setListId(Long listId) {
        this.listId = listId;
    }


}
