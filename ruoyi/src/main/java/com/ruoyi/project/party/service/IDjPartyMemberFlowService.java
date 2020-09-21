package com.ruoyi.project.party.service;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberFlow;

/**
 * 党员流动Service接口
 * 
 * @author ruoyi
 * @date 2020-09-21
 */
public interface IDjPartyMemberFlowService 
{
    /**
     * 查询党员流动
     * 
     * @param flowId 党员流动ID
     * @return 党员流动
     */
    public DjPartyMemberFlow selectDjPartyMemberFlowById(Long flowId);

    /**
     * 查询党员流动列表
     * 
     * @param djPartyMemberFlow 党员流动
     * @return 党员流动集合
     */
    public List<DjPartyMemberFlow> selectDjPartyMemberFlowList(DjPartyMemberFlow djPartyMemberFlow);

    /**
     * 新增党员流动
     * 
     * @param djPartyMemberFlow 党员流动
     * @return 结果
     */
    public int insertDjPartyMemberFlow(DjPartyMemberFlow djPartyMemberFlow);

    /**
     * 修改党员流动
     * 
     * @param djPartyMemberFlow 党员流动
     * @return 结果
     */
    public int updateDjPartyMemberFlow(DjPartyMemberFlow djPartyMemberFlow);

    /**
     * 批量删除党员流动
     * 
     * @param flowIds 需要删除的党员流动ID
     * @return 结果
     */
    public int deleteDjPartyMemberFlowByIds(Long[] flowIds);

    /**
     * 删除党员流动信息
     * 
     * @param flowId 党员流动ID
     * @return 结果
     */
    public int deleteDjPartyMemberFlowById(Long flowId);
}
