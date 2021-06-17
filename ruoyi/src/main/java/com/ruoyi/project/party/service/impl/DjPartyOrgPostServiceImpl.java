package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyOrgPostMapper;
import com.ruoyi.project.party.domain.DjPartyOrgPost;
import com.ruoyi.project.party.service.IDjPartyOrgPostService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 党组织职务Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-08
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyOrgPostServiceImpl implements IDjPartyOrgPostService
{
    @Autowired
    private DjPartyOrgPostMapper djPartyOrgPostMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;

    /**
     * 查询党组织职务
     *
     * @param id 党组织职务ID
     * @return 党组织职务
     */
    @Override
    public DjPartyOrgPost selectDjPartyOrgPostById(Long id)
    {
        DjPartyOrgPost partyOrgPost = djPartyOrgPostMapper.selectDjPartyOrgPostById(id);
        partyOrgPost.setDjPartyMember(
                djPartyMemberService.selectDjPartyMemberById(partyOrgPost.getPartyMemberId()));
        return partyOrgPost;
    }

    /**
     * 查询党组织职务列表
     *
     * @param djPartyOrgPost 党组织职务
     * @return 党组织职务
     */
    @Override
    public List<DjPartyOrgPost> selectDjPartyOrgPostList(DjPartyOrgPost djPartyOrgPost)
    {

        List<DjPartyOrgPost> list= djPartyOrgPostMapper.selectDjPartyOrgPostList(djPartyOrgPost);
        list.stream().forEach(partyOrgPost ->{
            partyOrgPost.setDjPartyMember(
                    djPartyMemberService.selectDjPartyMemberById(partyOrgPost.getPartyMemberId()));
        });
        return list;
    }

    /**
     * 新增党组织职务
     *
     * @param djPartyOrgPost 党组织职务
     * @return 结果
     */
    @Override
    public int insertDjPartyOrgPost(DjPartyOrgPost djPartyOrgPost)
    {
        djPartyOrgPost.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djPartyOrgPost.setCreateTime(DateUtils.getNowDate());
        return djPartyOrgPostMapper.insertDjPartyOrgPost(djPartyOrgPost);
    }

    /**
     * 修改党组织职务
     *
     * @param djPartyOrgPost 党组织职务
     * @return 结果
     */
    @Override
    public int updateDjPartyOrgPost(DjPartyOrgPost djPartyOrgPost)
    {
        djPartyOrgPost.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyOrgPost.setUpdateTime(DateUtils.getNowDate());
        return djPartyOrgPostMapper.updateDjPartyOrgPost(djPartyOrgPost);
    }

    /**
     * 批量删除党组织职务
     *
     * @param ids 需要删除的党组织职务ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyOrgPostByIds(Long[] ids)
    {
        return djPartyOrgPostMapper.deleteDjPartyOrgPostByIds(ids);
    }

    /**
     * 删除党组织职务信息
     *
     * @param id 党组织职务ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyOrgPostById(Long id)
    {
        return djPartyOrgPostMapper.deleteDjPartyOrgPostById(id);
    }
}
