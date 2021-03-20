package com.ruoyi.project.party.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 参与培训党员对象 dj_party_train_member
 *
 * @author ruoyi
 * @date 2021-03-09
 */
@Data
public class DjPartyTrainMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long trainMemberId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trainUuid;

    /** 组织机构 */
    @Excel(name = "组织机构")
    private Long partyOrgId;

    private DjPartyOrg partyOrg ;

    /** 党组织成员id */
    @Excel(name = "党组织成员id")
    private Long partyMemberId;

    private DjPartyMember partyMember ;

    /** 积分 */
    @Excel(name = "积分")
    private Integer score;

    private Integer rankNum;

    /** 参与党员状态 */
    @Excel(name = "参与党员状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
