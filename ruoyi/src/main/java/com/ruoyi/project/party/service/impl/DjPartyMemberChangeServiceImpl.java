package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberChangeMapper;
import com.ruoyi.project.party.domain.DjPartyMemberChange;
import com.ruoyi.project.party.service.IDjPartyMemberChangeService;

/**
 * 党员变更表Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-16
 */
@Service
public class DjPartyMemberChangeServiceImpl implements IDjPartyMemberChangeService
{
    @Autowired
    private DjPartyMemberChangeMapper djPartyMemberChangeMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询党员变更表
     *
     * @param memberId 党员变更表ID
     * @return 党员变更表
     */
    @Override
    public DjPartyMemberChange selectDjPartyMemberChangeById(Long memberId)
    {
        DjPartyMemberChange partyMemberChange = djPartyMemberChangeMapper.selectDjPartyMemberChangeById(memberId);
        if(StringUtils.isNotNull(partyMemberChange.getPartyOrgId())){
            partyMemberChange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMemberChange.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(partyMemberChange.getDeptId())){
            partyMemberChange.setSysDept(deptService.selectDeptById(partyMemberChange.getDeptId()));
        }
        return partyMemberChange;
    }

    @Override
    public DjPartyMemberChange selectDjPartyMemberChangeByMemberUuid(String memberUuid)
    {
        DjPartyMemberChange partyMemberChange = djPartyMemberChangeMapper.selectDjPartyMemberChangeByMemberUuid(memberUuid);
        if(StringUtils.isNotNull(partyMemberChange.getPartyOrgId())){
            partyMemberChange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMemberChange.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(partyMemberChange.getDeptId())){
            partyMemberChange.setSysDept(deptService.selectDeptById(partyMemberChange.getDeptId()));
        }
        return partyMemberChange;
    }

    /**
     * 查询党员变更表列表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 党员变更表
     */
    @Override
    public List<DjPartyMemberChange> selectDjPartyMemberChangeList(DjPartyMemberChange djPartyMemberChange)
    {
        List<DjPartyMemberChange> list = djPartyMemberChangeMapper.selectDjPartyMemberChangeList(djPartyMemberChange);
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
     * 新增党员变更表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberChange(DjPartyMemberChange djPartyMemberChange)
    {
        djPartyMemberChange.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberChange.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberChangeMapper.insertDjPartyMemberChange(djPartyMemberChange);
    }

    /**
     * 修改党员变更表
     *
     * @param djPartyMemberChange 党员变更表
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberChange(DjPartyMemberChange djPartyMemberChange)
    {
        djPartyMemberChange.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberChange.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberChangeMapper.updateDjPartyMemberChange(djPartyMemberChange);
    }

    /**
     * 批量删除党员变更表
     *
     * @param memberIds 需要删除的党员变更表ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberChangeByIds(Long[] memberIds)
    {
        return djPartyMemberChangeMapper.deleteDjPartyMemberChangeByIds(memberIds);
    }

    /**
     * 删除党员变更表信息
     *
     * @param memberId 党员变更表ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberChangeById(Long memberId)
    {
        return djPartyMemberChangeMapper.deleteDjPartyMemberChangeById(memberId);
    }
}
