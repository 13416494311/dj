package com.ruoyi.project.activity.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 活动纪要对象 dj_activity_summary
 * 
 * @author ruoyi
 * @date 2020-08-22
 */
public class DjActivitySummary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动纪要ID */
    private Long summaryId;

    /** 活动uuid */
    private String detailedUuid;

    /** 记录内容 */
    @Excel(name = "记录内容")
    private String recordContent;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSummaryId(Long summaryId) 
    {
        this.summaryId = summaryId;
    }

    public Long getSummaryId() 
    {
        return summaryId;
    }
    public void setDetailedUuid(String detailedUuid) 
    {
        this.detailedUuid = detailedUuid;
    }

    public String getDetailedUuid() 
    {
        return detailedUuid;
    }
    public void setRecordContent(String recordContent) 
    {
        this.recordContent = recordContent;
    }

    public String getRecordContent() 
    {
        return recordContent;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("summaryId", getSummaryId())
            .append("detailedUuid", getDetailedUuid())
            .append("recordContent", getRecordContent())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
