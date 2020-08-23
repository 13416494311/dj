package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityExperienceMapper;
import com.ruoyi.project.activity.domain.DjActivityExperience;
import com.ruoyi.project.activity.service.IDjActivityExperienceService;

/**
 * 心得体会Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@Service
public class DjActivityExperienceServiceImpl implements IDjActivityExperienceService
{
    @Autowired
    private DjActivityExperienceMapper djActivityExperienceMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询心得体会
     *
     * @param experienceId 心得体会ID
     * @return 心得体会
     */
    @Override
    public DjActivityExperience selectDjActivityExperienceById(Long experienceId)
    {
        return djActivityExperienceMapper.selectDjActivityExperienceById(experienceId);
    }

    /**
     * 查询心得体会列表
     *
     * @param djActivityExperience 心得体会
     * @return 心得体会
     */
    @Override
    public List<DjActivityExperience> selectDjActivityExperienceList(DjActivityExperience djActivityExperience)
    {
        List<DjActivityExperience> list = djActivityExperienceMapper.selectDjActivityExperienceList(djActivityExperience);
        list.stream().forEach(eperience->{
            if(StringUtils.isNotNull(eperience.getPartyMemberId())){
                eperience.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(eperience.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(eperience.getExperienceUuid())){
                SysFile sysFile = new SysFile();
                sysFile.setUuid(eperience.getExperienceUuid());
                eperience.setSysFiles(sysFileService.selectSysFileList(sysFile));
            }
        });

        return list;

    }

    /**
     * 新增心得体会
     *
     * @param djActivityExperience 心得体会
     * @return 结果
     */
    @Override
    public int insertDjActivityExperience(DjActivityExperience djActivityExperience)
    {
        djActivityExperience.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityExperience.setCreateTime(DateUtils.getNowDate());
        return djActivityExperienceMapper.insertDjActivityExperience(djActivityExperience);
    }

    /**
     * 修改心得体会
     *
     * @param djActivityExperience 心得体会
     * @return 结果
     */
    @Override
    public int updateDjActivityExperience(DjActivityExperience djActivityExperience)
    {
        djActivityExperience.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityExperience.setUpdateTime(DateUtils.getNowDate());
        return djActivityExperienceMapper.updateDjActivityExperience(djActivityExperience);
    }

    /**
     * 批量删除心得体会
     *
     * @param experienceIds 需要删除的心得体会ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityExperienceByIds(Long[] experienceIds)
    {
        return djActivityExperienceMapper.deleteDjActivityExperienceByIds(experienceIds);
    }

    /**
     * 删除心得体会信息
     *
     * @param experienceId 心得体会ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityExperienceById(Long experienceId)
    {
        return djActivityExperienceMapper.deleteDjActivityExperienceById(experienceId);
    }
}
