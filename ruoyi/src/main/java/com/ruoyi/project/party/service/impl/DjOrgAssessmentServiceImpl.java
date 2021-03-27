package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentMapper;
import com.ruoyi.project.party.domain.DjOrgAssessment;
import com.ruoyi.project.party.service.IDjOrgAssessmentService;

/**
 * 党组织考核Service业务层处理
 * 
 * @author admin
 * @date 2021-03-10
 */
@Service
public class DjOrgAssessmentServiceImpl implements IDjOrgAssessmentService 
{
    @Autowired
    private DjOrgAssessmentMapper djOrgAssessmentMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;

    /**
     * 查询党组织考核
     * 
     * @param id 党组织考核ID
     * @return 党组织考核
     */
    @Override
    public DjOrgAssessment selectDjOrgAssessmentById(Long id)
    {
        return djOrgAssessmentMapper.selectDjOrgAssessmentById(id);
    }
    /**
     * 查询党组织考核
     *
     * @param uuid 党组织考核ID
     * @return 党组织考核
     */
    @Override
    public DjOrgAssessment selectDjOrgAssessmentByUuid(String uuid)
    {
        return djOrgAssessmentMapper.selectDjOrgAssessmentByUuid(uuid);
    }

    /**
     * 查询党组织考核列表
     * 
     * @param djOrgAssessment 党组织考核
     * @return 党组织考核
     */
    @Override
    public List<DjOrgAssessment> selectDjOrgAssessmentList(DjOrgAssessment djOrgAssessment)
    {
        List<DjOrgAssessment> list = djOrgAssessmentMapper.selectDjOrgAssessmentList(djOrgAssessment);

        list.stream().forEach(arrange->{
            if(StringUtils.isNotNull(arrange.getPartyOrgId())){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
        });
        return list;
    }

    /**
     * 新增党组织考核
     * 
     * @param djOrgAssessment 党组织考核
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessment(DjOrgAssessment djOrgAssessment)
    {
        djOrgAssessment.setDelFlag("0");
        djOrgAssessment.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessment.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentMapper.insertDjOrgAssessment(djOrgAssessment);
    }

    /**
     * 修改党组织考核
     * 
     * @param djOrgAssessment 党组织考核
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessment(DjOrgAssessment djOrgAssessment)
    {
        djOrgAssessment.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessment.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentMapper.updateDjOrgAssessment(djOrgAssessment);
    }

    /**
     * 批量删除党组织考核
     * 
     * @param ids 需要删除的党组织考核ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentByIds(Long[] ids)
    {
        return djOrgAssessmentMapper.deleteDjOrgAssessmentByIds(ids);
    }

    /**
     * 删除党组织考核信息
     * 
     * @param id 党组织考核ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentById(Long id)
    {
        return djOrgAssessmentMapper.deleteDjOrgAssessmentById(id);
    }
}
