package com.ruoyi.project.party.mapper;

import com.ruoyi.project.party.domain.DjPartyMember;

import java.util.List;

/**
 * 党员信息Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
public interface DjPartyMemberMapper
{
    /**
     * 查询党员信息
     *
     * @param memberId 党员信息ID
     * @return 党员信息
     */
    public DjPartyMember selectDjPartyMemberById(Long memberId);

    /**
     * 查询党员信息列表
     *
     * @param djPartyMember 党员信息
     * @return 党员信息集合
     */
    public List<DjPartyMember> selectDjPartyMemberList(DjPartyMember djPartyMember);

    public List<DjPartyMember> selectPartyMemberList(DjPartyMember djPartyMember);
    /**
     * 新增党员信息
     *
     * @param djPartyMember 党员信息
     * @return 结果
     */
    public int insertDjPartyMember(DjPartyMember djPartyMember);

    /**
     * 修改党员信息
     *
     * @param djPartyMember 党员信息
     * @return 结果
     */
    public int updateDjPartyMember(DjPartyMember djPartyMember);

    /**
     * 删除党员信息
     *
     * @param memberId 党员信息ID
     * @return 结果
     */
    public int deleteDjPartyMemberById(Long memberId);

    /**
     * 批量删除党员信息
     *
     * @param memberIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberByIds(Long[] memberIds);

    /**
     * 校验党员姓名是否唯一
     *
     * @param memberName 党员姓名
     * @return 结果
     */
    public DjPartyMember checkMemberNameUnique(String memberName);

    /**
     * 校验手机号码是否唯一
     *
     * @param mobile 手机号码
     * @return 结果
     */
    public DjPartyMember checkMobileUnique(String mobile);

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @return 结果
     */
    public DjPartyMember checkEmailUnique(String email);

    /**
     * 校验身份证号是否唯一
     *
     * @param identityCard 身份证号
     * @return 结果
     */
    public DjPartyMember checkIdentityCardUnique(String identityCard);


}
