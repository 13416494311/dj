package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityMemberMapper;
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.service.IDjActivityMemberService;

/**
 * 活动参与人Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Service
public class DjActivityMemberServiceImpl implements IDjActivityMemberService
{
    @Autowired
    private DjActivityMemberMapper djActivityMemberMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;

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
}
