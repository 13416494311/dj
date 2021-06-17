package com.ruoyi.project.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysCommentMapper;
import com.ruoyi.project.system.domain.SysComment;
import com.ruoyi.project.system.service.ISysCommentService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 点评Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-19
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class SysCommentServiceImpl implements ISysCommentService
{
    @Autowired
    private SysCommentMapper sysCommentMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 查询点评
     *
     * @param id 点评ID
     * @return 点评
     */
    @Override
    public SysComment selectSysCommentById(Long id)
    {
        return sysCommentMapper.selectSysCommentById(id);
    }

    /**
     * 查询点评列表
     *
     * @param sysComment 点评
     * @return 点评
     */
    @Override
    public List<SysComment> selectSysCommentList(SysComment sysComment)
    {
        return sysCommentMapper.selectSysCommentList(sysComment);
    }

    @Override
    public List<SysComment> selectSysCommentTree(SysComment sysComment)
    {
        List<SysComment> list = sysCommentMapper.selectSysCommentList(sysComment);
        list.stream().forEach(comment -> {
            if(comment.getCommentUserId()!=null){
                SysUser commentUser = sysUserMapper.selectUserById(comment.getCommentUserId());
                comment.setCommentUser(commentUser);
            }
            if(comment.getTargetUserId()!=null){
                SysUser targetUser = sysUserMapper.selectUserById(comment.getTargetUserId());
                comment.setTargetUser(targetUser);
            }
        });
        List<SysComment> pList = list.stream().filter(comment -> comment.getParentId()== null).collect(Collectors.toList());
        List<SysComment> cList = list.stream().filter(comment -> comment.getParentId()!= null).collect(Collectors.toList());
        pList.stream().forEach(comment -> {
            List<SysComment> childrenList = new ArrayList<SysComment>();
            cList.stream().forEach(comment1 ->{
                if(comment1.getParentId().equals(comment.getId())){
                    childrenList.add(comment1);
                }
            } );
            comment.setChildrenList(childrenList);
        });
        return pList;
    }

    /**
     * 新增点评
     *
     * @param sysComment 点评
     * @return 结果
     */
    @Override
    public int insertSysComment(SysComment sysComment)
    {

        sysComment.setCommentUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        sysComment.setCreateDate(new Date());
        return sysCommentMapper.insertSysComment(sysComment);
    }

    /**
     * 修改点评
     *
     * @param sysComment 点评
     * @return 结果
     */
    @Override
    public int updateSysComment(SysComment sysComment)
    {
        return sysCommentMapper.updateSysComment(sysComment);
    }

    /**
     * 批量删除点评
     *
     * @param ids 需要删除的点评ID
     * @return 结果
     */
    @Override
    public int deleteSysCommentByIds(Long[] ids)
    {
        return sysCommentMapper.deleteSysCommentByIds(ids);
    }

    /**
     * 删除点评信息
     *
     * @param id 点评ID
     * @return 结果
     */
    @Override
    public int deleteSysCommentById(Long id)
    {
        return sysCommentMapper.deleteSysCommentById(id);
    }
}
