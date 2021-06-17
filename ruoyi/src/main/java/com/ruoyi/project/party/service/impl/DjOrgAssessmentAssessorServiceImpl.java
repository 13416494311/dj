package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentAssessorMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentAssessor;
import com.ruoyi.project.party.service.IDjOrgAssessmentAssessorService;

import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 党组织述职评议Service业务层处理
 *
 * @author admin
 * @date 2021-03-23
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentAssessorServiceImpl implements IDjOrgAssessmentAssessorService
{
    @Autowired
    private DjOrgAssessmentAssessorMapper djOrgAssessmentAssessorMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;

    /**
     * 查询党组织述职评议
     *
     * @param id 党组织述职评议ID
     * @return 党组织述职评议
     */
    @Override
    public DjOrgAssessmentAssessor selectDjOrgAssessmentAssessorById(Long id)
    {
        return djOrgAssessmentAssessorMapper.selectDjOrgAssessmentAssessorById(id);
    }

    /**
     * 查询党组织述职评议列表
     *
     * @param djOrgAssessmentAssessor 党组织述职评议
     * @return 党组织述职评议
     */
    @Override
    public List<DjOrgAssessmentAssessor> selectDjOrgAssessmentAssessorList(DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {

        List<DjOrgAssessmentAssessor> list = djOrgAssessmentAssessorMapper.selectDjOrgAssessmentAssessorList(djOrgAssessmentAssessor);

        list.stream().forEach( arrange ->{
            if(arrange.getPartyOrgId()!=null){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
            if(arrange.getAssessorId()!=null){
                DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(arrange.getAssessorId());
                arrange.setDjPartyMember(partyMember);
            }
        });
        return list;

    }

    /**
     * 新增党组织述职评议
     *
     * @param djOrgAssessmentAssessor 党组织述职评议
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentAssessor(DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        djOrgAssessmentAssessor.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentAssessorMapper.insertDjOrgAssessmentAssessor(djOrgAssessmentAssessor);
    }

    /**
     * 修改党组织述职评议
     *
     * @param djOrgAssessmentAssessor 党组织述职评议
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentAssessor(DjOrgAssessmentAssessor djOrgAssessmentAssessor)
    {
        djOrgAssessmentAssessor.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentAssessorMapper.updateDjOrgAssessmentAssessor(djOrgAssessmentAssessor);
    }

    /**
     * 批量删除党组织述职评议
     *
     * @param ids 需要删除的党组织述职评议ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentAssessorByIds(Long[] ids)
    {
        return djOrgAssessmentAssessorMapper.deleteDjOrgAssessmentAssessorByIds(ids);
    }

    /**
     * 删除党组织述职评议信息
     *
     * @param id 党组织述职评议ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentAssessorById(Long id)
    {
        return djOrgAssessmentAssessorMapper.deleteDjOrgAssessmentAssessorById(id);
    }
}
