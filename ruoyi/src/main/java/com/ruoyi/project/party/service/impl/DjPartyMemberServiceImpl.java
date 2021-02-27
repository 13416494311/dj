package com.ruoyi.project.party.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.members.domain.DjPartyMemberHelp;
import com.ruoyi.project.members.mapper.DjPartyMemberHelpMapper;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysDeptMapper;
import com.ruoyi.project.system.service.*;
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
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private DjPartyMemberHelpMapper partyMemberHelpMapper;

    @Override
    public int getMemberCount(){
        return djPartyMemberMapper.getMemberCount();
    }

    @Override
    public DjPartyMember selectPartyMemberById(Long memberId)
    {
        DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberId);
        if(StringUtils.isNotNull(partyMember.getDeptId())){
            partyMember.setSysDept(deptService.selectDeptById(partyMember.getDeptId()));
        }
        SysUser sysUser = userService.selectUserByPartyMemberId(memberId);
        partyMember.setSysUser(sysUser);
        return partyMember;
    }

    /**
     * 查询党员信息
     *
     * @param memberId 党员信息ID
     * @return 党员信息
     */
    @Override
    public DjPartyMember selectDjPartyMemberById(Long memberId)
    {
        DjPartyMember member = djPartyMemberMapper.selectDjPartyMemberById(memberId);
        if(StringUtils.isNotNull(member.getPartyOrgId())){
            member.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(member.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(member.getDeptId())){
            member.setSysDept(deptService.selectDeptById(member.getDeptId()));
        }
        if(member.getMemberType()!=null){
            member.setMemberTypeText(dictDataService.selectDictLabel("party_member_type",member.getMemberType()));
        }
        if(member.getAdministrativePosition()!=null){
            member.setAdministrativePositionText(dictDataService.selectDictLabel("administrative_position_type",member.getAdministrativePosition()));
        }
        if(member.getPostId()!=null){
            member.setSysPost(postService.selectPostById(member.getPostId()));
        }
        if(member.getEducation()!=null){
            member.setEducationText(dictDataService.selectDictLabel("education_type",member.getEducation()));
        }
        if(member.getEducation()!=null){
            member.setEducationText(dictDataService.selectDictLabel("education_type",member.getEducation()));
        }
        if(member.getNation()!=null){
            member.setNationText(dictDataService.selectDictLabel("nation_type",member.getNation()));
        }
        if(member.getPolity()!=null){
            member.setPolityText(dictDataService.selectDictLabel("polity_type",member.getPolity()));
        }

        DjPartyMemberHelp partyMemberHelp = new DjPartyMemberHelp();
        partyMemberHelp.setPartyMemberId(memberId);
        List<DjPartyMemberHelp> memberHelpList = partyMemberHelpMapper.selectDjPartyMemberHelpList(partyMemberHelp);
        //没有创建过帮扶数据
        if(memberHelpList!=null && memberHelpList.size()>0){
            member.setMemberHelp(memberHelpList.get(0));
        }
        return member;
    }

    @Override
    public List<DjPartyMember> getDjPartyMemberList(DjPartyMember djPartyMember)
    {
        List<DjPartyMember> partyMemberList = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
        partyMemberList.stream().forEach(partyMember -> {
            String deptAdminiPosFullName ="";
            if(partyMember.getDeptId()!=null){
                SysDept sysDept = sysDeptMapper.selectDeptById(partyMember.getDeptId());
                String[] sysDeptIds =sysDept.getAncestors().split(",");
                for(String sysDeptId:sysDeptIds){
                    if(!"100".equals(sysDeptId)&&!"0".equals(sysDeptId)){
                        deptAdminiPosFullName+=sysDeptMapper.selectDeptById(Long.parseLong(sysDeptId)).getDeptName()+"/";
                    }
                }
                deptAdminiPosFullName+=sysDept.getDeptName();
            }
            if(partyMember.getAdministrativePosition()!=null){
                deptAdminiPosFullName +="  "+ dictDataService.selectDictLabel("administrative_position_type",partyMember.getAdministrativePosition());
            }
            partyMember.setDeptAdminiPosFullName(deptAdminiPosFullName);
        });

        return partyMemberList;
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
        List<DjPartyMember> list = new ArrayList<DjPartyMember>();
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if(SecurityUtils.isAdmin(sysUser.getUserId())||SecurityUtils.isPartyOrgAll()){
            list = djPartyMemberMapper.selectDjPartyMemberList(djPartyMember);
        }else{
            djPartyMember.setPartyOrgId(sysUser.getDjPartyMember().getPartyOrgId());
            list = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
        }

        list.stream().forEach( member ->{
            if(member.getPartyOrgId()!=null){
                member.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(member.getPartyOrgId()));
            }
            if(member.getDeptId()!=null){
                member.setSysDept(deptService.selectDeptById(member.getDeptId()));
            }
            if(member.getMemberType()!=null){
                member.setMemberTypeText(dictDataService.selectDictLabel("party_member_type",member.getMemberType()));
            }
        });
        return list;
    }

    @Override
    public List<DjPartyMember> selectPartyMemberList(DjPartyMember djPartyMember)
    {

        List<DjPartyMember> list = new ArrayList<DjPartyMember>();
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if(SecurityUtils.isAdmin(sysUser.getUserId())||SecurityUtils.isPartyOrgAll()){
            list = djPartyMemberMapper.selectDjPartyMemberList(djPartyMember);
        }else{
            djPartyMember.setPartyOrgId(sysUser.getDjPartyMember().getPartyOrgId());
            list = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
        }
        list.stream().forEach( member ->{
            if(member.getPartyOrgId()!=null){
                member.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(member.getPartyOrgId()));
            }
            if(member.getDeptId()!=null){
                member.setSysDept(deptService.selectDeptById(member.getDeptId()));
            }
            if(member.getMemberType()!=null){
                member.setMemberTypeText(dictDataService.selectDictLabel("party_member_type",member.getMemberType()));
            }
            if(member.getAdministrativePosition()!=null){
                member.setAdministrativePositionText(dictDataService.selectDictLabel("administrative_position_type",member.getAdministrativePosition()));
            }
            if(member.getPostId()!=null){
                member.setSysPost(postService.selectPostById(member.getPostId()));
            }
            if(member.getEducation()!=null){
                member.setEducationText(dictDataService.selectDictLabel("education_type",member.getEducation()));
            }
            if(member.getEducation()!=null){
                member.setEducationText(dictDataService.selectDictLabel("education_type",member.getEducation()));
            }
            if(member.getNation()!=null){
                member.setNationText(dictDataService.selectDictLabel("nation_type",member.getNation()));
            }
            if(member.getPolity()!=null){
                member.setPolityText(dictDataService.selectDictLabel("polity_type",member.getPolity()));
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
        //生活困难
        if("1".equals(djPartyMember.getLifeDifficulty())){
            DjPartyMemberHelp memberHelp = new DjPartyMemberHelp();
            memberHelp.setHelpUuid(UUID.randomUUID().toString());
            memberHelp.setPartyMemberId(djPartyMember.getMemberId());
            memberHelp.setPartyOrgId(djPartyMember.getPartyOrgId());
            partyMemberHelpMapper.insertDjPartyMemberHelp(memberHelp);
        }


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
     * 新增党员信息
     *
     * @param djPartyMember 党员信息
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberForApp(DjPartyMember djPartyMember,String password)
    {
        djPartyMember.setPartyOrgId((long) 52);
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
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setRoleIds(new Long[]{Long.valueOf(3)});  //设置普通角色
        sysUser.setPostIds(new Long[]{Long.valueOf(4)});  //设置普通员工
        sysUser.setCreateTime(DateUtils.getNowDate());
        return userService.insertUserForApp(sysUser);
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

        //生活困难
        if("Y".equals(djPartyMember.getLifeDifficulty())){
            DjPartyMember partyMember =djPartyMemberMapper.selectDjPartyMemberById(djPartyMember.getMemberId());
            //之前不是生活困难
            if(!"Y".equals(partyMember.getLifeDifficulty())){
                DjPartyMemberHelp partyMemberHelp = new DjPartyMemberHelp();
                partyMemberHelp.setPartyMemberId(djPartyMember.getMemberId());
                List<DjPartyMemberHelp> memberHelpList = partyMemberHelpMapper.selectDjPartyMemberHelpList(partyMemberHelp);
                //没有创建过帮扶数据
                if(memberHelpList==null || memberHelpList.size()==0){
                    DjPartyMemberHelp memberHelp = new DjPartyMemberHelp();
                    memberHelp.setHelpUuid(UUID.randomUUID().toString());
                    memberHelp.setPartyMemberId(djPartyMember.getMemberId());
                    memberHelp.setPartyOrgId(djPartyMember.getPartyOrgId());
                    partyMemberHelpMapper.insertDjPartyMemberHelp(memberHelp);
                }
            }
        }

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

    @Override
    public List<Map<String,Object>> echarts_31(){
        return djPartyMemberMapper.echarts_31();
    }

    @Override
    public List<Map<String,Object>> echarts_32(){
        return djPartyMemberMapper.echarts_32();
    }

    @Override
    public List<Map<String,Object>> echarts_33(){
        return djPartyMemberMapper.echarts_33();
    }



}
