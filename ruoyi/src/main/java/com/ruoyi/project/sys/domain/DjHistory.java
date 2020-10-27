package com.ruoyi.project.sys.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党史对象 dj_history
 * 
 * @author ruoyi
 * @date 2020-10-27
 */
public class DjHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 历史ID */
    private Long historyId;

    /** 月 */
    @Excel(name = "月")
    private String month;

    /** 日 */
    @Excel(name = "日")
    private String day;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setHistoryId(Long historyId) 
    {
        this.historyId = historyId;
    }

    public Long getHistoryId() 
    {
        return historyId;
    }
    public void setMonth(String month) 
    {
        this.month = month;
    }

    public String getMonth() 
    {
        return month;
    }
    public void setDay(String day) 
    {
        this.day = day;
    }

    public String getDay() 
    {
        return day;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
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
            .append("historyId", getHistoryId())
            .append("month", getMonth())
            .append("day", getDay())
            .append("title", getTitle())
            .append("content", getContent())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
