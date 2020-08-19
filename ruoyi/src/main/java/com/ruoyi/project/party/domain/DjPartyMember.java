package com.ruoyi.project.party.domain;

import java.util.Date;

import com.ruoyi.project.system.domain.SysDept;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员信息对象 dj_party_member
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Data
public class DjPartyMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long memberId;

    /** 党组织ID */
    @Excel(name = "党组织ID")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 工号 */
    private String workNo;

    /** 党员照片 */
    private String avatar;

    /** 党员姓名 */
    @Excel(name = "党员姓名")
    private String memberName;

    /** 用户性别（0男 1女 2未知） */
    private String sex;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 身份证号 */
    private String identityCard;

    /** 出生日期 */
    private Date birthday;

    /** 所在单位 */
    private String companyName;

    /** 行政组织 */
    @Excel(name = "行政组织")
    private Long deptId;

    private SysDept sysDept;

    /** 行政职务 */
    private String administrativePosition;

    /** 职称 */
    private String title;

    /** 岗位 */
    private Long postId;

    /** 参加工作日期 */
    private Date workingDate;

    /** 民族 */
    private String nation;

    /** 政治面貌 */
    private String polity;

    /** 身份 */
    private String workIdentity;

    /** 学历 */
    private String education;

    /** 学位 */
    private String academicDegree;

    /** 籍贯 */
    private String nativePlace;

    /** 家庭住址 */
    private String homeAddress;

    /** 固定电话 */
    private String housePhone;

    /** 电子邮箱 */
    private String email;

    /** qq */
    private String qq;

    /** 微信号码 */
    private String wechat;

    /** 党员类型 */
    @Excel(name = "党员类型")
    private String memberType;

    /** 党员状态 */
    @Excel(name = "党员状态")
    private String memberStatus;

    /** 加入党支部日期 */
    private Date joinBranchData;

    /** 加入党日期 */
    private Date joinData;

    /** 转为正式党员日期 */
    private Date formalData;

    /** 流动党员（1：是  0：否） */
    private String floatingType;

    /** 党员分组 */
    private String memberGroup;

    /** 生活困难（1：是  0：否） */
    private String lifeDifficulty;

    /** 组织认定（1：是  0：否） */
    private String cognizance;

    /** 经济状况 */
    private String economicSituation;

    /** 身体健康情况 */
    private String physicalHealth;

    /** 生活困难类型 */
    private String lifeDifficultyType;

    /** 享受帮扶 */
    private String enjoyHelp;

    /** 补助情况 */
    private String helpInfo;

    /** 具体情况描述 */
    private String detail;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}