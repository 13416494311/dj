package com.ruoyi.project.members.domain;

import com.ruoyi.project.party.domain.DjPartyMember;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.party.domain.DjPartyOrg;

/**
 * 党员特长对象 dj_party_specialty
 *
 * @author admin
 * @date 2021-02-19
 */
@Data
public class DjPartySpecialty extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long specialtyId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID ")
    private Long memberId;

    private DjPartyMember djPartyMember ;
    /**
     * 党组织id
     */
    @Excel(name = "党组织id")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /**
     * 特长类别
     */
    @Excel(name = "特长类别")
    private String specialtyCategory;

    /**
     * 特长描述
     */
    @Excel(name = "特长描述")
    private String specialtyContent;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;



}
