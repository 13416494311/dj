package com.ruoyi.project.sys.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * APP消息对象 dj_sys_message
 *
 * @author ruoyi
 * @date 2020-10-03
 */
@Data
public class DjSysMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long messageId;

    /** 消息唯一uuid */
    @Excel(name = "消息唯一uuid")
    private String messageUuid;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 消息类型  1:消息 2:通知 */
    @Excel(name = "消息类型  1:消息 2:通知")
    private int type;

    /** 0:全部平台，1：ios, 2：android */
    @Excel(name = "0:全部平台，1：ios, 2：android")
    private int platform;

    /** 推送组名，多个组用英文逗号隔开.默认:全部组。eg.group1,group2 */
    @Excel(name = "推送组名，多个组用英文逗号隔开.默认:全部组。eg.group1,group2")
    private String groupName;

    /** 推送用户id, 多个用户用英文逗号分隔，eg. user1,user2 */
    @Excel(name = "推送用户id, 多个用户用英文逗号分隔，eg. user1,user2")
    private String userIds;

    /** 是否已查看（0未查看 1已查看） */
    @Excel(name = "是否已查看", readConverterExp = "0=未查看,1=已查看")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
