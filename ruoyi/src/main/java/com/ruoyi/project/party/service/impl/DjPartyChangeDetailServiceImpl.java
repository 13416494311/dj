package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyChangeDetailMapper;
import com.ruoyi.project.party.domain.DjPartyChangeDetail;
import com.ruoyi.project.party.service.IDjPartyChangeDetailService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 换届职位详情Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyChangeDetailServiceImpl implements IDjPartyChangeDetailService
{
    @Autowired
    private DjPartyChangeDetailMapper djPartyChangeDetailMapper;
    @Autowired
    private DjPartyMemberMapper partyMemberMapper;

    /**
     * 查询换届职位详情
     *
     * @param changeDetailId 换届职位详情ID
     * @return 换届职位详情
     */
    @Override
    public DjPartyChangeDetail selectDjPartyChangeDetailById(Long changeDetailId)
    {
        DjPartyChangeDetail changeDetail = djPartyChangeDetailMapper.selectDjPartyChangeDetailById(changeDetailId);
        if(changeDetail.getPreMemberId()!=null){
            changeDetail.setPreMember(partyMemberMapper.selectDjPartyMemberById(changeDetail.getPreMemberId()));
        }
        if(changeDetail.getCurMemberId()!=null){
            changeDetail.setCurMember(partyMemberMapper.selectDjPartyMemberById(changeDetail.getCurMemberId()));
        }
        return changeDetail;
    }

    /**
     * 查询换届职位详情列表
     *
     * @param djPartyChangeDetail 换届职位详情
     * @return 换届职位详情
     */
    @Override
    public List<DjPartyChangeDetail> selectDjPartyChangeDetailList(DjPartyChangeDetail djPartyChangeDetail)
    {
        List<DjPartyChangeDetail> changeDetailList = djPartyChangeDetailMapper.selectDjPartyChangeDetailList(djPartyChangeDetail);
        changeDetailList.stream().forEach(changeDetail->{
            if(changeDetail.getPreMemberId()!=null){
                changeDetail.setPreMember(partyMemberMapper.selectDjPartyMemberById(changeDetail.getPreMemberId()));
            }
            if(changeDetail.getCurMemberId()!=null){
                changeDetail.setCurMember(partyMemberMapper.selectDjPartyMemberById(changeDetail.getCurMemberId()));
            }
        });
        return changeDetailList;
    }

    /**
     * 新增换届职位详情
     *
     * @param djPartyChangeDetail 换届职位详情
     * @return 结果
     */
    @Override
    public int insertDjPartyChangeDetail(DjPartyChangeDetail djPartyChangeDetail)
    {
        djPartyChangeDetail.setCreateTime(DateUtils.getNowDate());
        return djPartyChangeDetailMapper.insertDjPartyChangeDetail(djPartyChangeDetail);
    }

    /**
     * 修改换届职位详情
     *
     * @param djPartyChangeDetail 换届职位详情
     * @return 结果
     */
    @Override
    public int updateDjPartyChangeDetail(DjPartyChangeDetail djPartyChangeDetail)
    {
        djPartyChangeDetail.setUpdateTime(DateUtils.getNowDate());
        return djPartyChangeDetailMapper.updateDjPartyChangeDetail(djPartyChangeDetail);
    }

    /**
     * 批量删除换届职位详情
     *
     * @param changeDetailIds 需要删除的换届职位详情ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyChangeDetailByIds(Long[] changeDetailIds)
    {
        return djPartyChangeDetailMapper.deleteDjPartyChangeDetailByIds(changeDetailIds);
    }

    /**
     * 删除换届职位详情信息
     *
     * @param changeDetailId 换届职位详情ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyChangeDetailById(Long changeDetailId)
    {
        return djPartyChangeDetailMapper.deleteDjPartyChangeDetailById(changeDetailId);
    }
}
