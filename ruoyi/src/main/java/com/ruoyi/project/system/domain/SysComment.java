package com.ruoyi.project.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 点评对象 sys_comment
 *
 * @author ruoyi
 * @date 2021-02-19
 */
@Data
public class SysComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "关联业务uuid")
    private String businessUuid;

    /** 1：点赞  2：评论 */
    @Excel(name = "1：点赞  2：评论")
    private String commentType;

    /** $column.columnComment */
    @Excel(name = "回复父id")
    private Long parentId;

    /** 评论人id */
    @Excel(name = "评论人id")
    private Long commentUserId;

    private CommentUser commentUser;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /** 回复人id */
    @Excel(name = "回复人id")
    private Long targetUserId;

    private CommentUser targetUser;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    private List<SysComment> childrenList;



    public void setCommentUser(SysUser sysUser) {
        this.commentUser = new CommentUser(sysUser.getUserId(),sysUser.getNickName(),sysUser.getAvatar());
    }


    public void setTargetUser(SysUser sysUser) {
        this.targetUser = new CommentUser(sysUser.getUserId(),sysUser.getNickName(),sysUser.getAvatar());;
    }
}

@Data
class CommentUser{
    private Long id;

    private String nickName;

    private String avatar;

    CommentUser(){
    }

    CommentUser(Long id  , String nickName ,String avatar){
        this.id = id;
        this.nickName = nickName;
        this.avatar = avatar;
    }
}



