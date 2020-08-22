package com.ruoyi.project.activity.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动决议对象 dj_activity_resolution
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Data
public class DjActivityResolution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动决议ID */
    private Long resolutionId;

    /** 活动ID */
    private String detailedUuid;

    /** 决议内容 */
    @Excel(name = "决议内容")
    private String recordContent;

    /** 完成情况 */
    @Excel(name = "完成情况")
    private String resolutionStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
