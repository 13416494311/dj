package com.ruoyi.project.sys.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.SysUser;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 操作日志对象 dj_sys_log
 *
 * @author ruoyi
 * @date 2020-09-18
 */
@Data
public class DjSysLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String uuid;

    /** 步骤(cur：当前  his：历史) */
    @Excel(name = "步骤(cur：当前  his：历史)")
    private String stepType;

    /** 步骤名称 */
    @Excel(name = "步骤名称")
    private String stepName;

    /** 操作人 */
    @Excel(name = "操作人")
    private Long operUserId;

    private SysUser sysUser;

    /** 具体操作结果 */
    @Excel(name = "具体操作结果")
    private String operResult;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 备注 */
    @Excel(name = "备注")
    private String operReason;


}
