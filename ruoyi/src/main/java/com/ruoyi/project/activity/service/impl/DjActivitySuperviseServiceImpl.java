package com.ruoyi.project.activity.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivitySuperviseMapper;
import com.ruoyi.project.activity.domain.DjActivitySupervise;
import com.ruoyi.project.activity.service.IDjActivitySuperviseService;

/**
 * 活动督办Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@Service
public class DjActivitySuperviseServiceImpl implements IDjActivitySuperviseService
{
    @Autowired
    private DjActivitySuperviseMapper djActivitySuperviseMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    /**
     * 查询活动督办
     *
     * @param superviseId 活动督办ID
     * @return 活动督办
     */
    @Override
    public DjActivitySupervise selectDjActivitySuperviseById(Long superviseId)
    {
        DjActivitySupervise activitySupervise = djActivitySuperviseMapper.
                selectDjActivitySuperviseById(superviseId);
        if(StringUtils.isNotNull(activitySupervise.getPartyMemberId())){
            activitySupervise.setDjPartyMember(djPartyMemberService.
                    selectDjPartyMemberById(activitySupervise.getPartyMemberId()));
        }
        return activitySupervise;
    }

    /**
     * 查询活动督办列表
     *
     * @param djActivitySupervise 活动督办
     * @return 活动督办
     */
    @Override
    public List<DjActivitySupervise> selectDjActivitySuperviseList(DjActivitySupervise djActivitySupervise)
    {
        List<DjActivitySupervise> superviseList = djActivitySuperviseMapper.selectDjActivitySuperviseList(djActivitySupervise);
        superviseList.stream().forEach(activitySupervise->{
            if(StringUtils.isNotNull(activitySupervise.getPartyMemberId())){
                activitySupervise.setDjPartyMember(djPartyMemberService.
                        selectDjPartyMemberById(activitySupervise.getPartyMemberId()));
            }
        });
        return superviseList;
    }

    /**
     * 新增活动督办
     *
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    @Override
    public int insertDjActivitySupervise(DjActivitySupervise djActivitySupervise)
    {
        djActivitySupervise.setCreateTime(DateUtils.getNowDate());
        return djActivitySuperviseMapper.insertDjActivitySupervise(djActivitySupervise);
    }

    /**
     * 修改活动督办
     *
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    @Override
    public int updateDjActivitySupervise(DjActivitySupervise djActivitySupervise)
    {
        djActivitySupervise.setUpdateTime(DateUtils.getNowDate());
        return djActivitySuperviseMapper.updateDjActivitySupervise(djActivitySupervise);
    }

    /**
     * 批量删除活动督办
     *
     * @param superviseIds 需要删除的活动督办ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuperviseByIds(Long[] superviseIds)
    {
        return djActivitySuperviseMapper.deleteDjActivitySuperviseByIds(superviseIds);
    }

    /**
     * 删除活动督办信息
     *
     * @param superviseId 活动督办ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuperviseById(Long superviseId)
    {
        return djActivitySuperviseMapper.deleteDjActivitySuperviseById(superviseId);
    }
}
