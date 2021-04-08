package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberEducationMapper;
import com.ruoyi.project.party.domain.DjPartyMemberEducation;
import com.ruoyi.project.party.service.IDjPartyMemberEducationService;

/**
 * 教育经历Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-07
 */
@Service
public class DjPartyMemberEducationServiceImpl implements IDjPartyMemberEducationService
{
    @Autowired
    private DjPartyMemberEducationMapper djPartyMemberEducationMapper;

    /**
     * 查询教育经历
     *
     * @param educationId 教育经历ID
     * @return 教育经历
     */
    @Override
    public DjPartyMemberEducation selectDjPartyMemberEducationById(Long educationId)
    {
        return djPartyMemberEducationMapper.selectDjPartyMemberEducationById(educationId);
    }

    /**
     * 查询教育经历列表
     *
     * @param djPartyMemberEducation 教育经历
     * @return 教育经历
     */
    @Override
    public List<DjPartyMemberEducation> selectDjPartyMemberEducationList(DjPartyMemberEducation djPartyMemberEducation)
    {
        return djPartyMemberEducationMapper.selectDjPartyMemberEducationList(djPartyMemberEducation);
    }

    /**
     * 新增教育经历
     *
     * @param djPartyMemberEducation 教育经历
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberEducation(DjPartyMemberEducation djPartyMemberEducation)
    {
        djPartyMemberEducation.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberEducation.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberEducationMapper.insertDjPartyMemberEducation(djPartyMemberEducation);
    }

    /**
     * 修改教育经历
     *
     * @param djPartyMemberEducation 教育经历
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberEducation(DjPartyMemberEducation djPartyMemberEducation)
    {
        djPartyMemberEducation.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberEducation.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberEducationMapper.updateDjPartyMemberEducation(djPartyMemberEducation);
    }

    /**
     * 批量删除教育经历
     *
     * @param educationIds 需要删除的教育经历ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberEducationByIds(Long[] educationIds)
    {
        return djPartyMemberEducationMapper.deleteDjPartyMemberEducationByIds(educationIds);
    }

    /**
     * 删除教育经历信息
     *
     * @param educationId 教育经历ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberEducationById(Long educationId)
    {
        return djPartyMemberEducationMapper.deleteDjPartyMemberEducationById(educationId);
    }
}
