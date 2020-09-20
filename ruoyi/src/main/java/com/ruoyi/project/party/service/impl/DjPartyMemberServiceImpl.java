package com.ruoyi.project.party.service.impl;

import java.util.List;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysDeptMapper;
import com.ruoyi.project.system.service.ISysConfigService;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 党员信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Service
public class DjPartyMemberServiceImpl implements IDjPartyMemberService
{
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询党员信息
     *
     * @param memberId 党员信息ID
     * @return 党员信息
     */
    @Override
    public DjPartyMember selectDjPartyMemberById(Long memberId)
    {
        DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberId);
        if(StringUtils.isNotNull(partyMember.getPartyOrgId())){
            partyMember.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMember.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(partyMember.getDeptId())){
            partyMember.setSysDept(deptService.selectDeptById(partyMember.getDeptId()));
        }
        return partyMember;
    }

    /**
     * 查询党员信息列表
     *
     * @param djPartyMember 党员信息
     * @return 党员信息
     */
    @Override
    public List<DjPartyMember> selectDjPartyMemberList(DjPartyMember djPartyMember)
    {

        List<DjPartyMember> list = djPartyMemberMapper.selectDjPartyMemberList(djPartyMember);
        list.stream().forEach( member ->{
            if(member.getPartyOrgId()!=null){
                member.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(member.getPartyOrgId()));
            }
            if(member.getDeptId()!=null){
                member.setSysDept(deptService.selectDeptById(member.getDeptId()));
            }
        });
        return list;
    }

    /**
     * 新增党员信息
     *
     * @param djPartyMember 党员信息
     * @return 结果
     */
    @Override
    public int insertDjPartyMember(DjPartyMember djPartyMember)
    {
        djPartyMember.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMember.setCreateTime(DateUtils.getNowDate());
        djPartyMemberMapper.insertDjPartyMember(djPartyMember);

        SysUser sysUser = new SysUser();
        sysUser.setAvatar(djPartyMember.getAvatar());
        sysUser.setDeptId(djPartyMember.getDeptId());
        sysUser.setUserName(djPartyMember.getMemberName());
        sysUser.setNickName(djPartyMember.getMemberName());
        sysUser.setPartyMemberId(djPartyMember.getMemberId());
        sysUser.setSex(djPartyMember.getSex());
        sysUser.setPhonenumber(djPartyMember.getMobile());
        sysUser.setEmail(djPartyMember.getEmail());
        String password = configService.selectConfigByKey("sys.user.initPassword");
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setRoleIds(new Long[]{Long.valueOf(3)});  //设置普通角色
        sysUser.setPostIds(new Long[]{Long.valueOf(4)});  //设置普通员工
        sysUser.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysUser.setCreateTime(DateUtils.getNowDate());
        return userService.insertUser(sysUser);
    }

    /**
     * 修改党员信息
     *
     * @param djPartyMember 党员信息
     * @return 结果
     */
    @Override
    public int updateDjPartyMember(DjPartyMember djPartyMember)
    {

        SysUser sysUser = userService.selectUserByPartyMemberId(djPartyMember.getMemberId());
        sysUser.setAvatar(djPartyMember.getAvatar());
        sysUser.setDeptId(djPartyMember.getDeptId());
        sysUser.setUserName(djPartyMember.getMemberName());
        sysUser.setNickName(djPartyMember.getMemberName());
        sysUser.setPartyMemberId(djPartyMember.getMemberId());
        sysUser.setSex(djPartyMember.getSex());
        sysUser.setPhonenumber(djPartyMember.getMobile());
        sysUser.setEmail(djPartyMember.getEmail());
        sysUser.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysUser.setUpdateTime(DateUtils.getNowDate());
        userService.updateUser(sysUser);

        djPartyMember.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMember.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberMapper.updateDjPartyMember(djPartyMember);
    }

    /**
     * 批量删除党员信息
     *
     * @param memberIds 需要删除的党员信息ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberByIds(Long[] memberIds)
    {
        return djPartyMemberMapper.deleteDjPartyMemberByIds(memberIds);
    }

    /**
     * 删除党员信息信息
     *
     * @param memberId 党员信息ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberById(Long memberId)
    {
        return djPartyMemberMapper.deleteDjPartyMemberById(memberId);
    }

    /**
     * 检查党员姓名唯一性
     *
     * @param djPartyMember
     * @return
     */
    public String checkPartyMemberNameUnique(DjPartyMember djPartyMember)
    {
        Long memberId = StringUtils.isNull(djPartyMember.getMemberId()) ? -1L : djPartyMember.getMemberId();
        DjPartyMember info = djPartyMemberMapper.checkMemberNameUnique(djPartyMember.getMemberName());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 检查党员手机号唯一性
     *
     * @param djPartyMember
     * @return
     */
    public String checkPartyMemberMobileUnique(DjPartyMember djPartyMember)
    {
        Long memberId = StringUtils.isNull(djPartyMember.getMemberId()) ? -1L : djPartyMember.getMemberId();
        DjPartyMember info = djPartyMemberMapper.checkMobileUnique(djPartyMember.getMobile());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 检查党员邮箱唯一性
     *
     * @param djPartyMember
     * @return
     */
    public String checkPartyMemberEmailUnique(DjPartyMember djPartyMember)
    {
        Long memberId = StringUtils.isNull(djPartyMember.getMemberId()) ? -1L : djPartyMember.getMemberId();
        DjPartyMember info = djPartyMemberMapper.checkEmailUnique(djPartyMember.getEmail());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 检查党员身份证号唯一性
     *
     * @param djPartyMember
     * @return
     */
    public String checkPartyMemberIdentityCardUnique(DjPartyMember djPartyMember)
    {
        Long memberId = StringUtils.isNull(djPartyMember.getMemberId()) ? -1L : djPartyMember.getMemberId();
        DjPartyMember info = djPartyMemberMapper.checkIdentityCardUnique(djPartyMember.getIdentityCard());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }




}
