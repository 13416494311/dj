package com.ruoyi.project.party.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.TreeEntity;

/**
 * 党组织架构对象 dj_party_org
 *
 * @author ruoyi
 * @date 2020-08-03
 */
@Data
public class DjPartyOrg extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 党组织id */
    private Long partyOrgId;

    private String partyOrgUuid;

    /** 党组织名称 */
    @Excel(name = "党组织名称")
    private String partyOrgName;

    private String partyOrgFullName;

    /** 组织类型 */
    @Excel(name = "组织类型")
    private String orgType;

    /** 党组织类别 */
    @Excel(name = "党组织类别")
    private String partyOrgType;

    /** 建立时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "建立时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buildTime;

    /** 负责人 */
    @Excel(name = "负责人")
    private Long leader;

    private DjPartyMember leaderMember;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "区域代码")
    private String regionCode;

    @Excel(name = "地址")
    private String address;

    /** 党组织简介 */
    @Excel(name = "党组织简介")
    private String description;

    /** 党组织状态（0正常 1停用） */
    @Excel(name = "党组织状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private List<DjPartyMember> partyPositionMemberList;
}
