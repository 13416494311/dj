package com.ruoyi.project.members.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 帮扶记录对象 dj_party_member_help_record
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
public class DjPartyMemberHelpRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long helpRecordId;

    /** 帮扶记录UUID */
    @Excel(name = "帮扶记录UUID")
    private String helpRecordUuid;

    /** 帮扶主表UUID */
    @Excel(name = "帮扶主表UUID")
    private String helpUuid;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date helpStartTime;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date helpEndTime;

    /** 帮扶形式 */
    @Excel(name = "帮扶形式")
    private String assistance;

    /** 帮扶记录 */
    @Excel(name = "帮扶记录")
    private String content;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setHelpRecordId(Long helpRecordId) 
    {
        this.helpRecordId = helpRecordId;
    }

    public Long getHelpRecordId() 
    {
        return helpRecordId;
    }
    public void setHelpRecordUuid(String helpRecordUuid) 
    {
        this.helpRecordUuid = helpRecordUuid;
    }

    public String getHelpRecordUuid() 
    {
        return helpRecordUuid;
    }
    public void setHelpUuid(String helpUuid) 
    {
        this.helpUuid = helpUuid;
    }

    public String getHelpUuid() 
    {
        return helpUuid;
    }
    public void setHelpStartTime(Date helpStartTime) 
    {
        this.helpStartTime = helpStartTime;
    }

    public Date getHelpStartTime() 
    {
        return helpStartTime;
    }
    public void setHelpEndTime(Date helpEndTime) 
    {
        this.helpEndTime = helpEndTime;
    }

    public Date getHelpEndTime() 
    {
        return helpEndTime;
    }
    public void setAssistance(String assistance) 
    {
        this.assistance = assistance;
    }

    public String getAssistance() 
    {
        return assistance;
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
            .append("helpRecordId", getHelpRecordId())
            .append("helpRecordUuid", getHelpRecordUuid())
            .append("helpUuid", getHelpUuid())
            .append("helpStartTime", getHelpStartTime())
            .append("helpEndTime", getHelpEndTime())
            .append("assistance", getAssistance())
            .append("content", getContent())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
