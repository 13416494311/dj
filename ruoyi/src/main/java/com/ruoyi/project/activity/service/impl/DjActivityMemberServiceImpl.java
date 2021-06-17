package com.ruoyi.project.activity.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.mapper.DjActivityDetailedMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityMemberMapper;
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.service.IDjActivityMemberService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动参与人Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjActivityMemberServiceImpl implements IDjActivityMemberService
{
    @Autowired
    private DjActivityMemberMapper djActivityMemberMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private DjActivityDetailedMapper djActivityDetailedMapper;
    /**
     * 查询活动参与人
     *
     * @param memberId 活动参与人ID
     * @return 活动参与人
     */
    @Override
    public DjActivityMember selectDjActivityMemberById(Long memberId)
    {
        return djActivityMemberMapper.selectDjActivityMemberById(memberId);
    }

    /**
     * 查询活动参与人列表
     *
     * @param djActivityMember 活动参与人
     * @return 活动参与人
     */
    @Override
    public List<DjActivityMember> selectDjActivityMemberList(DjActivityMember djActivityMember)
    {
        List<DjActivityMember> list = djActivityMemberMapper.selectDjActivityMemberList(djActivityMember);
        list.stream().forEach(activityMember->{
            if(StringUtils.isNotNull(activityMember.getPartyMemberId())){
                activityMember.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(activityMember.getPartyMemberId()));
            }
        });
        return list;
    }

    /**
     * 新增活动参与人
     *
     * @param djActivityMember 活动参与人
     * @return 结果
     */
    @Override
    public int insertDjActivityMember(DjActivityMember djActivityMember)
    {
        djActivityMember.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djActivityMember.setCreateTime(DateUtils.getNowDate());
        return djActivityMemberMapper.insertDjActivityMember(djActivityMember);
    }

    /**
     * 修改活动参与人
     *
     * @param djActivityMember 活动参与人
     * @return 结果
     */
    @Override
    public int updateDjActivityMember(DjActivityMember djActivityMember)
    {
        djActivityMember.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityMember.setUpdateTime(DateUtils.getNowDate());
        return djActivityMemberMapper.updateDjActivityMember(djActivityMember);
    }

    /**
     * 批量删除活动参与人
     *
     * @param memberIds 需要删除的活动参与人ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityMemberByIds(Long[] memberIds)
    {
        return djActivityMemberMapper.deleteDjActivityMemberByIds(memberIds);
    }

    /**
     * 删除活动参与人信息
     *
     * @param memberId 活动参与人ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityMemberById(Long memberId)
    {
        return djActivityMemberMapper.deleteDjActivityMemberById(memberId);
    }

    public String signIn(String detailedUuid){
        String result ="签到成功！";

        DjActivityDetailed activityDetailed = djActivityDetailedMapper.selectDjActivityDetailedByDetailedUuid(detailedUuid);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if(StringUtils.isNotNull(sysUser.getPartyMemberId())){
            DjActivityMember djActivityMember = new DjActivityMember();
            djActivityMember.setDetailedUuid(detailedUuid);
            djActivityMember.setPartyMemberId(sysUser.getPartyMemberId());
            List<DjActivityMember> activityMemberList  = djActivityMemberMapper.selectDjActivityMemberList(djActivityMember);

            //判断是否迟到
            String status = "2";
            if(activityDetailed.getActivityPlanStartTime() != null &&activityDetailed.getActivityPlanStartTime().before(new Date())){
                status="3";
            }

            if(activityMemberList!= null && activityMemberList.size()>0){
                DjActivityMember activityMember = activityMemberList.get(0);
                //如果没签到过
                if(!"2".equals(activityMember.getStatus())||!"3".equals(activityMember.getStatus())){
                    activityMember.setStatus(status);
                    activityMember.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
                    activityMember.setUpdateTime(new Date());
                    djActivityMemberMapper.updateDjActivityMember(activityMember);
                }else{
                    result = "您已签到！";
                }
            }else{
                djActivityMember.setType("2");
                djActivityMember.setStatus(status);
                djActivityMember.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
                djActivityMember.setCreateTime(new Date());
                djActivityMemberMapper.insertDjActivityMember(djActivityMember);
            }

        }else{
            result = "您无对应党员信息！";
        }
        return result;
    }

}
