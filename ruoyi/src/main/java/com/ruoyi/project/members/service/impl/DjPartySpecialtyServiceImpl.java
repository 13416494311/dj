package com.ruoyi.project.members.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.members.mapper.DjPartySpecialtyMapper;
import com.ruoyi.project.members.domain.DjPartySpecialty;
import com.ruoyi.project.members.service.IDjPartySpecialtyService;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;

/**
 * 党员特长Service业务层处理
 *
 * @author admin
 * @date 2021-02-19
 */
@Service
public class DjPartySpecialtyServiceImpl implements IDjPartySpecialtyService
{
    @Autowired
    private DjPartySpecialtyMapper djPartySpecialtyMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;

    /**
     * 查询党员特长
     *
     * @param specialtyId 党员特长ID
     * @return 党员特长
     */
    @Override
    public DjPartySpecialty selectDjPartySpecialtyById(Long specialtyId)
    {
        DjPartySpecialty partySpecialty = djPartySpecialtyMapper.selectDjPartySpecialtyById(specialtyId);
        if(partySpecialty.getPartyOrgId()!=null){
            partySpecialty.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partySpecialty.getPartyOrgId()));
        }
        if(partySpecialty.getMemberId()!=null){
            DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(partySpecialty.getMemberId());
            partySpecialty.setDjPartyMember(partyMember);
        }

        return partySpecialty;
    }

    /**
     * 查询党员特长列表
     *
     * @param djPartySpecialty 党员特长
     * @return 党员特长
     */
    @Override
    public List<DjPartySpecialty> selectDjPartySpecialtyList(DjPartySpecialty djPartySpecialty)
    {

        List<DjPartySpecialty> list = djPartySpecialtyMapper.selectDjPartySpecialtyList(djPartySpecialty);

        list.stream().forEach( partySpecialty ->{
            if(partySpecialty.getPartyOrgId()!=null){
                partySpecialty.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partySpecialty.getPartyOrgId()));
            }
            if(partySpecialty.getMemberId()!=null){
                DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(partySpecialty.getMemberId());
                partySpecialty.setDjPartyMember(partyMember);
            }
        });
        return list;
    }

    /**
     * 新增党员特长
     *
     * @param djPartySpecialty 党员特长
     * @return 结果
     */
    @Override
    public int insertDjPartySpecialty(DjPartySpecialty djPartySpecialty)
    {
        djPartySpecialty.setDelFlag("0");
        djPartySpecialty.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartySpecialty.setCreateTime(DateUtils.getNowDate());
        return djPartySpecialtyMapper.insertDjPartySpecialty(djPartySpecialty);
    }

    /**
     * 修改党员特长
     *
     * @param djPartySpecialty 党员特长
     * @return 结果
     */
    @Override
    public int updateDjPartySpecialty(DjPartySpecialty djPartySpecialty)
    {
        djPartySpecialty.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartySpecialty.setUpdateTime(DateUtils.getNowDate());
        return djPartySpecialtyMapper.updateDjPartySpecialty(djPartySpecialty);
    }

    /**
     * 批量删除党员特长
     *
     * @param specialtyIds 需要删除的党员特长ID
     * @return 结果
     */
    @Override
    public int deleteDjPartySpecialtyByIds(Long[] specialtyIds)
    {
        return djPartySpecialtyMapper.deleteDjPartySpecialtyByIds(specialtyIds);
    }

    /**
     * 删除党员特长信息
     *
     * @param specialtyId 党员特长ID
     * @return 结果
     */
    @Override
    public int deleteDjPartySpecialtyById(Long specialtyId)
    {
        return djPartySpecialtyMapper.deleteDjPartySpecialtyById(specialtyId);
    }
}
