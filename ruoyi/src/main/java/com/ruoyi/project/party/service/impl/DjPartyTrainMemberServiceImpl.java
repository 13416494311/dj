package com.ruoyi.project.party.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.impl.SysUserServiceImpl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyTrainMemberMapper;
import com.ruoyi.project.party.domain.DjPartyTrainMember;
import com.ruoyi.project.party.service.IDjPartyTrainMemberService;
import org.springframework.util.CollectionUtils;

/**
 * 参与培训党员Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-09
 */
@Service
public class DjPartyTrainMemberServiceImpl implements IDjPartyTrainMemberService
{
    @Autowired
    private DjPartyTrainMemberMapper djPartyTrainMemberMapper;
    @Autowired
    private IDjPartyOrgService partyOrgService;
    @Autowired
    private DjPartyMemberMapper partyMemberMapper;







    @Override
    public Map<String,Object> getTrainRankInfo(String rankType){
        Map<String,Object> result = new HashMap<>();

        Long partyMemberId =  SecurityUtils.getLoginUser().getUser().getPartyMemberId();
        if(partyMemberId== null ) {
            return null;
        }
        DjPartyMember partyMember = partyMemberMapper.selectDjPartyMemberById(partyMemberId);

        result.put("totalScore",getTotalScore(partyMemberId));

        result.put("infoScore",getInfoScore(partyMemberId,rankType));

        result.put("infoAllRank",getInfoAllRank(partyMemberId,rankType));

        result.put("infoSelfRank",getInfoSelfRank(partyMemberId,rankType,partyMember.getPartyOrgId()));

        result.put("trainRankList",getTrainRankList(partyMember.getPartyOrgId()));

        result.put("partyOrgCount",getPartyOrgCount(partyMember.getPartyOrgId()));

        return result;
    }


    private int getPartyOrgCount(Long partyOrgId){

        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setPartyOrgId(partyOrgId);
        List<DjPartyMember> djPartyMemberList = partyMemberMapper.selectDjPartyMemberList(djPartyMember);

        return djPartyMemberList.size();

    }


    private List<DjPartyTrainMember> getTrainRankList(Long partyOrgId){

        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setPartyOrgId(partyOrgId);
        Map<String,Object> params = new HashMap<>();
        params.put("rankType","2");
        djPartyMember.setParams(params);
        PageHelper.startPage(1, 8, null);
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.getTrainRankInfo(djPartyMember);
        partyTrainMemberList.stream().forEach(partyTrainMember -> {
            if (StringUtils.isNotNull(partyTrainMember.getPartyOrgId())) {
                partyTrainMember.setPartyOrg(partyOrgService.selectDjPartyOrgById(partyTrainMember.getPartyOrgId()));
            }
            if (StringUtils.isNotNull(partyTrainMember.getPartyMemberId())) {
                partyTrainMember.setPartyMember(partyMemberMapper.selectDjPartyMemberById(partyTrainMember.getPartyMemberId()));
            }
        });
        return partyTrainMemberList;
    }

    private int getInfoSelfRank(Long partyMemberId,String rankType,Long partyOrgId){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setMemberId(partyMemberId);
        djPartyMember.setPartyOrgId(partyOrgId);
        Map<String,Object> params = new HashMap<>();
        params.put("rankType",rankType);
        djPartyMember.setParams(params);
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.getTrainRankInfo(djPartyMember);
        if(!CollectionUtils.isEmpty(partyTrainMemberList)){
            DjPartyTrainMember partyTrainMember = partyTrainMemberList.get(0);
            return partyTrainMember.getRank()!=null?partyTrainMember.getRank():0 ;
        }else{
            return 0 ;
        }
    }

    private int getInfoAllRank(Long partyMemberId,String rankType){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setMemberId(partyMemberId);
        Map<String,Object> params = new HashMap<>();
        params.put("rankType",rankType);
        djPartyMember.setParams(params);
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.getTrainRankInfo(djPartyMember);
        if(!CollectionUtils.isEmpty(partyTrainMemberList)){
            DjPartyTrainMember partyTrainMember = partyTrainMemberList.get(0);
            return partyTrainMember.getRank()!=null?partyTrainMember.getRank():0 ;
        }else{
            return 0 ;
        }
    }


    private int getInfoScore(Long partyMemberId,String rankType){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setMemberId(partyMemberId);
        Map<String,Object> params = new HashMap<>();
        params.put("rankType",rankType);
        djPartyMember.setParams(params);
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.getTrainRankInfo(djPartyMember);
        if(!CollectionUtils.isEmpty(partyTrainMemberList)){
            DjPartyTrainMember partyTrainMember = partyTrainMemberList.get(0);
            return partyTrainMember.getScore()!=null?partyTrainMember.getScore():0 ;
        }else{
            return 0 ;
        }
    }

    private int getTotalScore(Long partyMemberId){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setMemberId(partyMemberId);
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.getTrainRankInfo(djPartyMember);
        if(!CollectionUtils.isEmpty(partyTrainMemberList)){
            DjPartyTrainMember partyTrainMember = partyTrainMemberList.get(0);
            return partyTrainMember.getScore()!=null?partyTrainMember.getScore():0 ;
        }else{
            return 0 ;
        }
    }
    /**
     * 查询参与培训党员
     *
     * @param trainMemberId 参与培训党员ID
     * @return 参与培训党员
     */
    @Override
    public DjPartyTrainMember selectDjPartyTrainMemberById(Long trainMemberId)
    {
        DjPartyTrainMember partyTrainMember = djPartyTrainMemberMapper.selectDjPartyTrainMemberById(trainMemberId);
        if (StringUtils.isNotNull(partyTrainMember.getPartyOrgId())) {
            partyTrainMember.setPartyOrg(partyOrgService.selectDjPartyOrgById(partyTrainMember.getPartyOrgId()));
        }
        if (StringUtils.isNotNull(partyTrainMember.getPartyMemberId())) {
            partyTrainMember.setPartyMember(partyMemberMapper.selectDjPartyMemberById(partyTrainMember.getPartyMemberId()));
        }
        return partyTrainMember;
    }

    /**
     * 查询参与培训党员列表
     *
     * @param djPartyTrainMember 参与培训党员
     * @return 参与培训党员
     */
    @Override
    public List<DjPartyTrainMember> selectDjPartyTrainMemberList(DjPartyTrainMember djPartyTrainMember)
    {
        List<DjPartyTrainMember> partyTrainMemberList = djPartyTrainMemberMapper.selectDjPartyTrainMemberList(djPartyTrainMember);
        partyTrainMemberList.stream().forEach(partyTrainMember -> {
            if (StringUtils.isNotNull(partyTrainMember.getPartyOrgId())) {
                partyTrainMember.setPartyOrg(partyOrgService.selectDjPartyOrgById(partyTrainMember.getPartyOrgId()));
            }
            if (StringUtils.isNotNull(partyTrainMember.getPartyMemberId())) {
                partyTrainMember.setPartyMember(partyMemberMapper.selectDjPartyMemberById(partyTrainMember.getPartyMemberId()));
            }
        });
        return partyTrainMemberList;
    }

    /**
     * 新增参与培训党员
     *
     * @param djPartyTrainMember 参与培训党员
     * @return 结果
     */
    @Override
    public int insertDjPartyTrainMember(DjPartyTrainMember djPartyTrainMember)
    {
        djPartyTrainMember.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyTrainMember.setCreateTime(DateUtils.getNowDate());
        return djPartyTrainMemberMapper.insertDjPartyTrainMember(djPartyTrainMember);
    }

    /**
     * 修改参与培训党员
     *
     * @param djPartyTrainMember 参与培训党员
     * @return 结果
     */
    @Override
    public int updateDjPartyTrainMember(DjPartyTrainMember djPartyTrainMember)
    {
        djPartyTrainMember.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyTrainMember.setUpdateTime(DateUtils.getNowDate());
        if("2".equals(djPartyTrainMember.getStatus())||"3".equals(djPartyTrainMember.getStatus())||"4".equals(djPartyTrainMember.getStatus())){
            djPartyTrainMember.setScore(1);
        }else{
            djPartyTrainMember.setScore(0);
        }
        return djPartyTrainMemberMapper.updateDjPartyTrainMember(djPartyTrainMember);
    }

    /**
     * 批量删除参与培训党员
     *
     * @param trainMemberIds 需要删除的参与培训党员ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyTrainMemberByIds(Long[] trainMemberIds)
    {
        return djPartyTrainMemberMapper.deleteDjPartyTrainMemberByIds(trainMemberIds);
    }

    /**
     * 删除参与培训党员信息
     *
     * @param trainMemberId 参与培训党员ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyTrainMemberById(Long trainMemberId)
    {
        return djPartyTrainMemberMapper.deleteDjPartyTrainMemberById(trainMemberId);
    }
}
