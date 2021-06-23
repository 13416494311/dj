package com.ruoyi.project.party.service.impl;

import java.util.List;
import java.util.UUID;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.domain.DjOrgAssessmentList;
import com.ruoyi.project.party.domain.DjOrgAssessmentListScore;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.mapper.DjOrgAssessmentListMapper;
import com.ruoyi.project.party.mapper.DjOrgAssessmentListScoreMapper;
import com.ruoyi.project.party.mapper.DjOrgAssessmentyearMapper;
import com.ruoyi.project.party.service.IDjOrgAssessmentListScoreService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentMapper;
import com.ruoyi.project.party.domain.DjOrgAssessment;
import com.ruoyi.project.party.service.IDjOrgAssessmentService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 党组织考核Service业务层处理
 *
 * @author admin
 * @date 2021-03-10
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentServiceImpl implements IDjOrgAssessmentService
{
    @Autowired
    private DjOrgAssessmentMapper djOrgAssessmentMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private DjOrgAssessmentyearMapper djOrgAssessmentyearMapper;
    @Autowired
    private IDjOrgAssessmentListScoreService djOrgAssessmentListScoreService;
    @Autowired
    private DjOrgAssessmentListMapper djOrgAssessmentListMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    /**
     * 查询党组织考核
     *
     * @param id 党组织考核ID
     * @return 党组织考核
     */
    @Override
    public DjOrgAssessment selectDjOrgAssessmentById(Long id)
    {
        DjOrgAssessment assessment = djOrgAssessmentMapper.selectDjOrgAssessmentById(id);

        if(assessment.getPartyOrgId()!=null){
            assessment.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(assessment.getPartyOrgId()));
        }

        if(StringUtils.isNotNull(assessment.getAssessmentyearUuid())){
            assessment.setAssessmentyear(djOrgAssessmentyearMapper.selectDjOrgAssessmentyearByUuid(assessment.getAssessmentyearUuid()));
        }

        return assessment;
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
        DjOrgAssessment assessment = djOrgAssessmentMapper.selectDjOrgAssessmentByUuid(uuid);

        if(assessment.getPartyOrgId()!=null){
            assessment.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(assessment.getPartyOrgId()));
        }

        if(StringUtils.isNotNull(assessment.getAssessmentyearUuid())){
            assessment.setAssessmentyear(djOrgAssessmentyearMapper.selectDjOrgAssessmentyearByUuid(assessment.getAssessmentyearUuid()));
        }

        return assessment;

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

        list.stream().forEach(assessment->{
            if(assessment.getPartyOrgId()!=null){
                assessment.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(assessment.getPartyOrgId()));
            }

            if(StringUtils.isNotNull(assessment.getAssessmentyearUuid())){
                assessment.setAssessmentyear(djOrgAssessmentyearMapper.selectDjOrgAssessmentyearByUuid(assessment.getAssessmentyearUuid()));
            }


        });
        return list;
    }

    @Override
    public List<DjOrgAssessment> selectDjOrgAssessmentListRank(DjOrgAssessment djOrgAssessment)
    {
        List<DjOrgAssessment> list = djOrgAssessmentMapper.selectDjOrgAssessmentListRank(djOrgAssessment);

        list.stream().forEach(assessment->{
            if(assessment.getPartyOrgId()!=null){
                assessment.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(assessment.getPartyOrgId()));
            }

            if(StringUtils.isNotNull(assessment.getAssessmentyearUuid())){
                assessment.setAssessmentyear(djOrgAssessmentyearMapper.selectDjOrgAssessmentyearByUuid(assessment.getAssessmentyearUuid()));
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

        if("3".equals(djOrgAssessment.getOrgAssessmentStatus())
                &&"0".equals(djOrgAssessment.getPerformanceAppraisalStatus())){
            Double score = djOrgAssessment.getAssessmentScore()*djOrgAssessment.getAssessmentScoreRatio();
            djOrgAssessment.setScore(score);

        }else if("3".equals(djOrgAssessment.getOrgAssessmentStatus())
                &&"2".equals(djOrgAssessment.getPerformanceAppraisalStatus())){
            Double score = djOrgAssessment.getAssessmentScore()*djOrgAssessment.getAssessmentScoreRatio() +
                    djOrgAssessment.getPerformanceAppraisalScore()*djOrgAssessment.getPerformanceAppraisalScoreRatio();
            djOrgAssessment.setScore(score);
        }

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

    @Override
    public void addByOrg(String assessmentyearUuid,String partyOrgIds){
        DjOrgAssessmentList djOrgAssessmentList = new DjOrgAssessmentList();
        djOrgAssessmentList.setStatus("0");
        djOrgAssessmentList.setType("1");
        //双项考评清单
        List<DjOrgAssessmentList> djOrgAssessmentListAll = djOrgAssessmentListMapper.selectDjOrgAssessmentListList(djOrgAssessmentList);
        djOrgAssessmentList.setType("2");
        //项目绩效考评清单
        List<DjOrgAssessmentList> djOrgAssessmentListAl2 = djOrgAssessmentListMapper.selectDjOrgAssessmentListList(djOrgAssessmentList);
        //双项考评占比
        Double assessmentScoreRatio= Double.parseDouble(dictDataService.selectDictLabel("assessment_ratio","1"));
        //项目绩效考评占比
        Double performanceAppraisalScoreRatio= Double.parseDouble(dictDataService.selectDictLabel("assessment_ratio","2"));
        for(String partyOrgId:partyOrgIds.split(",")){
            String uuid = UUID.randomUUID().toString();
            //创建党组织考核主表
            DjOrgAssessment djOrgAssessment =new DjOrgAssessment();
            djOrgAssessment.setAssessmentUuid(uuid);
            djOrgAssessment.setAssessmentyearUuid(assessmentyearUuid);
            djOrgAssessment.setPartyOrgId(Long.parseLong(partyOrgId));
            djOrgAssessment.setOrgAssessmentStatus("0");
            djOrgAssessment.setPerformanceAppraisalStatus("0");
            //创建党组织考核项打分表
            DjOrgAssessmentListScore djOrgAssessmentListScore = new DjOrgAssessmentListScore();
            djOrgAssessmentListScore.setAssessmentUuid(uuid);
            //双项考评打分表
            for (DjOrgAssessmentList djOrgAssessmentListOne:djOrgAssessmentListAll) {
                djOrgAssessmentListScore.setAssessmentUuid(uuid);
                djOrgAssessmentListScore.setType(djOrgAssessmentListOne.getType());
                djOrgAssessmentListScore.setItem(djOrgAssessmentListOne.getItem());
                djOrgAssessmentListScore.setContent(djOrgAssessmentListOne.getContent());
                djOrgAssessmentListScore.setQuota(djOrgAssessmentListOne.getQuota());
                djOrgAssessmentListScore.setScore(djOrgAssessmentListOne.getScore());
                djOrgAssessmentListScore.setCriteria(djOrgAssessmentListOne.getCriteria());
                djOrgAssessmentListScore.setOrderNum(djOrgAssessmentListOne.getOrderNum());
                djOrgAssessmentListScoreService.insertDjOrgAssessmentListScore(djOrgAssessmentListScore);
            }
            //项目绩效考核打分表
            DjPartyOrg partyOrg = djPartyOrgService.selectDjPartyOrgById(Long.parseLong(partyOrgId));
            if("Y".equals(partyOrg.getPerformanceAppraisal())){
                djOrgAssessmentListAl2.stream().forEach(djOrgAssessmentListTwo -> {
                    djOrgAssessmentListScore.setAssessmentUuid(uuid);
                    djOrgAssessmentListScore.setType(djOrgAssessmentListTwo.getType());
                    djOrgAssessmentListScore.setItem(djOrgAssessmentListTwo.getItem());
                    djOrgAssessmentListScore.setContent(djOrgAssessmentListTwo.getContent());
                    djOrgAssessmentListScore.setQuota(djOrgAssessmentListTwo.getQuota());
                    djOrgAssessmentListScore.setScore(djOrgAssessmentListTwo.getScore());
                    djOrgAssessmentListScore.setCriteria(djOrgAssessmentListTwo.getCriteria());
                    djOrgAssessmentListScore.setOrderNum(djOrgAssessmentListTwo.getOrderNum());
                    djOrgAssessmentListScoreService.insertDjOrgAssessmentListScore(djOrgAssessmentListScore);
                });
                djOrgAssessment.setAssessmentScoreRatio(assessmentScoreRatio);
                djOrgAssessment.setPerformanceAppraisalScoreRatio(performanceAppraisalScoreRatio);
                djOrgAssessment.setPerformanceAppraisalStatus("1");
            }else{
                djOrgAssessment.setAssessmentScoreRatio((double) 1);
            }
            this.insertDjOrgAssessment(djOrgAssessment);
        }
    }


}
