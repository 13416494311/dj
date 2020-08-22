package com.ruoyi.project.sys.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 待办对象 dj_sys_todo
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Data
public class DjSysTodo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long todoId;

    private String uuid;

    /** 待办类型 */
    @Excel(name = "待办类型")
    private String type;

    /** 待办标题 */
    @Excel(name = "待办标题")
    private String title;

    /** 跳转地址name */
    @Excel(name = "跳转地址name")
    private String urlName;

    /** 跳转组件path */
    @Excel(name = "跳转组件path")
    private String urlPath;

    /** 参数json字符串 */
    @Excel(name = "参数json字符串")
    private String urlParams;

    /** 完成状态（0：未完成 1：已完成） */
    @Excel(name = "完成状态", readConverterExp = "0=：未完成,1=：已完成")
    private String status;

    /** 处理人userid */
    @Excel(name = "处理人userid")
    private Long userId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
