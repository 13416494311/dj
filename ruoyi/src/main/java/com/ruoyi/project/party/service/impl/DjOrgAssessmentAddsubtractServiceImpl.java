package com.ruoyi.project.party.service.impl;

import java.util.List;
import java.util.UUID;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.service.IDjOrgAssessmentService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentAddsubtractMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentAddsubtract;
import com.ruoyi.project.party.service.IDjOrgAssessmentAddsubtractService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 党组织加减分审核Service业务层处理
 *
 * @author admin
 * @date 2021-03-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentAddsubtractServiceImpl implements IDjOrgAssessmentAddsubtractService
{
    @Autowired
    private DjOrgAssessmentAddsubtractMapper djOrgAssessmentAddsubtractMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjOrgAssessmentService djOrgAssessmentService;

    /**
     * 查询党组织加减分审核
     *
     * @param addsubtractId 党组织加减分审核ID
     * @return 党组织加减分审核
     */
    @Override
    public DjOrgAssessmentAddsubtract selectDjOrgAssessmentAddsubtractById(Long addsubtractId)
    {
        return djOrgAssessmentAddsubtractMapper.selectDjOrgAssessmentAddsubtractById(addsubtractId);
    }

    /**
     * 查询党组织加减分审核列表
     *
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 党组织加减分审核
     */
    @Override
    public List<DjOrgAssessmentAddsubtract> selectDjOrgAssessmentAddsubtractList(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        List<DjOrgAssessmentAddsubtract> djOrgAssessmentAddsubtractList =djOrgAssessmentAddsubtractMapper.selectDjOrgAssessmentAddsubtractList(djOrgAssessmentAddsubtract);
        djOrgAssessmentAddsubtractList.stream().forEach( assessmentAddsubtract ->{
            if(assessmentAddsubtract.getPartyOrgId()!=null){
                assessmentAddsubtract.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(assessmentAddsubtract.getPartyOrgId()));
            }
            if(assessmentAddsubtract.getAssessmentUuid()!=null){
                assessmentAddsubtract.setDjOrgAssessment(djOrgAssessmentService.selectDjOrgAssessmentByUuid(assessmentAddsubtract.getAssessmentUuid()));
            }
        });
        return djOrgAssessmentAddsubtractList;
    }

    /**
     * 新增党组织加减分审核
     *
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentAddsubtract(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        djOrgAssessmentAddsubtract.setDelFlag("0");
        djOrgAssessmentAddsubtract.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentAddsubtract.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentAddsubtractMapper.insertDjOrgAssessmentAddsubtract(djOrgAssessmentAddsubtract);
    }

    /**
     * 修改党组织加减分审核
     *
     * @param djOrgAssessmentAddsubtract 党组织加减分审核
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentAddsubtract(DjOrgAssessmentAddsubtract djOrgAssessmentAddsubtract)
    {
        djOrgAssessmentAddsubtract.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentAddsubtract.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentAddsubtractMapper.updateDjOrgAssessmentAddsubtract(djOrgAssessmentAddsubtract);
    }

    /**
     * 批量删除党组织加减分审核
     *
     * @param addsubtractIds 需要删除的党组织加减分审核ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentAddsubtractByIds(Long[] addsubtractIds)
    {
        return djOrgAssessmentAddsubtractMapper.deleteDjOrgAssessmentAddsubtractByIds(addsubtractIds);
    }

    /**
     * 删除党组织加减分审核信息
     *
     * @param addsubtractId 党组织加减分审核ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentAddsubtractById(Long addsubtractId)
    {
        return djOrgAssessmentAddsubtractMapper.deleteDjOrgAssessmentAddsubtractById(addsubtractId);
    }
}
