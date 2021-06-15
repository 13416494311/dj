package com.ruoyi.project.party.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.SysDept;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党员变更表对象 dj_party_member_change
 *
 * @author ruoyi
 * @date 2020-09-16
 */
@Data
public class DjPartyMemberChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long memberId;

    /** 用户唯一uuid */
    @Excel(name = "用户唯一uuid")
    private String memberUuid;

    /** 党员变更类型（add：新增 ；edit：修改；del：删除） */
    @Excel(name = "党员变更类型", readConverterExp = "add：新增；edit：修改；del：删除")
    private String changeType;

    /** 关联党组织成员id */
    @Excel(name = "关联党组织成员id")
    private Long partyMemberId;

    private String partyMemberUuid;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String auditState;

    /** 党组织ID */
    @Excel(name = "党组织ID")
    private Long partyOrgId;

    private DjPartyOrg djPartyOrg;

    /** 工号 */
    @Excel(name = "工号")
    private String workNo;

    /** 党员照片 */
    @Excel(name = "党员照片")
    private String avatar;

    /** 党员姓名 */
    @Excel(name = "党员姓名")
    private String memberName;

    /** 用户性别（0男 1女 2未知） */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String identityCard;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 所在单位 */
    @Excel(name = "所在单位")
    private String companyName;

    /** 部门 */
    @Excel(name = "部门")
    private Long deptId;

    private SysDept sysDept;

    /** 职务 */
    @Excel(name = "职务")
    private String administrativePosition;

    /** 党内职务 */
    private String partyPositionType;

    /** 职称 */
    @Excel(name = "职称")
    private String title;

    /** 岗位 */
    @Excel(name = "岗位")
    private Long postId;

    /** 参加工作日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "参加工作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workingDate;

    /** 民族 */
    @Excel(name = "民族")
    private String nation;

    /** 政治面貌 */
    @Excel(name = "政治面貌")
    private String polity;

    /** 身份 */
    @Excel(name = "身份")
    private String workIdentity;

    /** 学历 */
    @Excel(name = "学历")
    private String education;

    /** 学位 */
    @Excel(name = "学位")
    private String academicDegree;

    /** 籍贯 */
    @Excel(name = "籍贯")
    private String nativePlace;

    /** 家庭住址 */
    @Excel(name = "家庭住址")
    private String homeAddress;

    /** 固定电话 */
    @Excel(name = "固定电话")
    private String housePhone;

    /** 电子邮箱 */
    @Excel(name = "电子邮箱")
    private String email;

    /** qq */
    @Excel(name = "qq")
    private String qq;

    /** 微信号码 */
    @Excel(name = "微信号码")
    private String wechat;

    /** 党员类型 */
    @Excel(name = "党员类型")
    private String memberType;

    /** 在岗状态 */
    @Excel(name = "在岗状态")
    private String memberStatus;

    /** 加入党支部日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入党支部日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinBranchData;

    /** 加入党日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入党日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinData;

    /** 成为入党积极分子日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date preMemberData;

    /** 转为正式党员日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "转为正式党员日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date formalData;

    /** 流动党员（1：是  0：否） */
    @Excel(name = "流动党员", readConverterExp = "1=：是,0=：否")
    private String floatingType;

    /** 党员分组 */
    @Excel(name = "党员分组")
    private String memberGroup;

    /** 上一个党组织ID */
    private Long prePartyOrgId;

    /** 生活困难（1：是  0：否） */
    @Excel(name = "生活困难", readConverterExp = "1=：是,0=：否")
    private String lifeDifficulty;

    /** 组织认定（1：是  0：否） */
    @Excel(name = "组织认定", readConverterExp = "1=：是,0=：否")
    private String cognizance;

    /** 经济状况 */
    @Excel(name = "经济状况")
    private String economicSituation;

    /** 身体健康情况 */
    @Excel(name = "身体健康情况")
    private String physicalHealth;

    /** 生活困难类型 */
    @Excel(name = "生活困难类型")
    private String lifeDifficultyType;

    /** 享受帮扶 */
    @Excel(name = "享受帮扶")
    private String enjoyHelp;

    /** 补助情况 */
    @Excel(name = "补助情况")
    private String helpInfo;

    /** 具体情况描述 */
    @Excel(name = "具体情况描述")
    private String detail;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}
