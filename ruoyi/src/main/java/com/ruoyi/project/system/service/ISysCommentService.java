package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysComment;

/**
 * 点评Service接口
 *
 * @author ruoyi
 * @date 2021-02-19
 */
public interface ISysCommentService
{
    /**
     * 查询点评
     *
     * @param id 点评ID
     * @return 点评
     */
    public SysComment selectSysCommentById(Long id);

    /**
     * 查询点评列表
     *
     * @param sysComment 点评
     * @return 点评集合
     */
    public List<SysComment> selectSysCommentList(SysComment sysComment);

    public List<SysComment> selectSysCommentTree(SysComment sysComment);

    /**
     * 新增点评
     *
     * @param sysComment 点评
     * @return 结果
     */
    public int insertSysComment(SysComment sysComment);

    /**
     * 修改点评
     *
     * @param sysComment 点评
     * @return 结果
     */
    public int updateSysComment(SysComment sysComment);

    /**
     * 批量删除点评
     *
     * @param ids 需要删除的点评ID
     * @return 结果
     */
    public int deleteSysCommentByIds(Long[] ids);

    /**
     * 删除点评信息
     *
     * @param id 点评ID
     * @return 结果
     */
    public int deleteSysCommentById(Long id);
}
