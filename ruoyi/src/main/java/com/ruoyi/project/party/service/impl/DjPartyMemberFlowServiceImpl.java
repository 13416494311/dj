package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberFlowMapper;
import com.ruoyi.project.party.domain.DjPartyMemberFlow;
import com.ruoyi.project.party.service.IDjPartyMemberFlowService;

/**
 * 党员流动Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-21
 */
@Service
public class DjPartyMemberFlowServiceImpl implements IDjPartyMemberFlowService
{
    @Autowired
    private DjPartyMemberFlowMapper djPartyMemberFlowMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;

    /**
     * 查询党员流动
     *
     * @param flowId 党员流动ID
     * @return 党员流动
     */
    @Override
    public DjPartyMemberFlow selectDjPartyMemberFlowById(Long flowId)
    {
        DjPartyMemberFlow partyMemberFlow=djPartyMemberFlowMapper.selectDjPartyMemberFlowById(flowId);
        if(StringUtils.isNotNull(partyMemberFlow.getFlowMemberId())){
            partyMemberFlow.setFlowMember(djPartyMemberService.selectDjPartyMemberById(partyMemberFlow.getFlowMemberId()));
        }
        return partyMemberFlow;
    }

    /**
     * 查询党员流动列表
     *
     * @param djPartyMemberFlow 党员流动
     * @return 党员流动
     */
    @Override
    public List<DjPartyMemberFlow> selectDjPartyMemberFlowList(DjPartyMemberFlow djPartyMemberFlow)
    {
        List<DjPartyMemberFlow> partyMemberFlowList = djPartyMemberFlowMapper.selectDjPartyMemberFlowList(djPartyMemberFlow);
        partyMemberFlowList.stream().forEach(partyMemberFlow->{
            if(StringUtils.isNotNull(partyMemberFlow.getFlowMemberId())){
                partyMemberFlow.setFlowMember(djPartyMemberService.selectDjPartyMemberById(partyMemberFlow.getFlowMemberId()));
            }
        });
        return partyMemberFlowList;
    }

    /**
     * 新增党员流动
     *
     * @param djPartyMemberFlow 党员流动
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberFlow(DjPartyMemberFlow djPartyMemberFlow)
    {
        djPartyMemberFlow.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberFlowMapper.insertDjPartyMemberFlow(djPartyMemberFlow);
    }

    /**
     * 修改党员流动
     *
     * @param djPartyMemberFlow 党员流动
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberFlow(DjPartyMemberFlow djPartyMemberFlow)
    {
        djPartyMemberFlow.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberFlowMapper.updateDjPartyMemberFlow(djPartyMemberFlow);
    }

    /**
     * 批量删除党员流动
     *
     * @param flowIds 需要删除的党员流动ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberFlowByIds(Long[] flowIds)
    {
        return djPartyMemberFlowMapper.deleteDjPartyMemberFlowByIds(flowIds);
    }

    /**
     * 删除党员流动信息
     *
     * @param flowId 党员流动ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberFlowById(Long flowId)
    {
        return djPartyMemberFlowMapper.deleteDjPartyMemberFlowById(flowId);
    }
}
