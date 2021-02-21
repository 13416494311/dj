package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberPoliticalBirthdayMapper;
import com.ruoyi.project.party.domain.DjPartyMemberPoliticalBirthday;
import com.ruoyi.project.party.service.IDjPartyMemberPoliticalBirthdayService;

/**
 * 政治生日Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-21
 */
@Service
public class DjPartyMemberPoliticalBirthdayServiceImpl implements IDjPartyMemberPoliticalBirthdayService
{
    @Autowired
    private DjPartyMemberPoliticalBirthdayMapper djPartyMemberPoliticalBirthdayMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    /**
     * 查询政治生日
     *
     * @param birthdayId 政治生日ID
     * @return 政治生日
     */
    @Override
    public DjPartyMemberPoliticalBirthday selectDjPartyMemberPoliticalBirthdayById(Long birthdayId)
    {
        DjPartyMemberPoliticalBirthday politicalBirthday = djPartyMemberPoliticalBirthdayMapper.selectDjPartyMemberPoliticalBirthdayById(birthdayId);
        if(StringUtils.isNotNull(politicalBirthday.getMemberId())){
            politicalBirthday.setPartyMember(djPartyMemberService.selectPartyMemberById(politicalBirthday.getMemberId()));

        }
        if(StringUtils.isNotNull(politicalBirthday.getPartyOrgId())){
            politicalBirthday.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(politicalBirthday.getPartyOrgId()));
        }
        return politicalBirthday;
    }

    /**
     * 查询政治生日列表
     *
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 政治生日
     */
    @Override
    public List<DjPartyMemberPoliticalBirthday> selectDjPartyMemberPoliticalBirthdayList(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        List<DjPartyMemberPoliticalBirthday> politicalBirthdayList = djPartyMemberPoliticalBirthdayMapper.selectDjPartyMemberPoliticalBirthdayList(djPartyMemberPoliticalBirthday);
        politicalBirthdayList.stream().forEach(politicalBirthday->{
            if(StringUtils.isNotNull(politicalBirthday.getMemberId())){
                politicalBirthday.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(politicalBirthday.getMemberId()));
            }
            if(StringUtils.isNotNull(politicalBirthday.getPartyOrgId())){
                politicalBirthday.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(politicalBirthday.getPartyOrgId()));
            }
        });
        return politicalBirthdayList;
    }

    /**
     * 新增政治生日
     *
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberPoliticalBirthday(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        djPartyMemberPoliticalBirthday.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberPoliticalBirthday.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberPoliticalBirthdayMapper.insertDjPartyMemberPoliticalBirthday(djPartyMemberPoliticalBirthday);
    }

    /**
     * 修改政治生日
     *
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberPoliticalBirthday(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday)
    {
        djPartyMemberPoliticalBirthday.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberPoliticalBirthday.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberPoliticalBirthdayMapper.updateDjPartyMemberPoliticalBirthday(djPartyMemberPoliticalBirthday);
    }

    /**
     * 批量删除政治生日
     *
     * @param birthdayIds 需要删除的政治生日ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberPoliticalBirthdayByIds(Long[] birthdayIds)
    {
        return djPartyMemberPoliticalBirthdayMapper.deleteDjPartyMemberPoliticalBirthdayByIds(birthdayIds);
    }

    /**
     * 删除政治生日信息
     *
     * @param birthdayId 政治生日ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberPoliticalBirthdayById(Long birthdayId)
    {
        return djPartyMemberPoliticalBirthdayMapper.deleteDjPartyMemberPoliticalBirthdayById(birthdayId);
    }
}
