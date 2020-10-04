package com.ruoyi.project.sys.service;

import java.util.List;
import com.ruoyi.project.sys.domain.DjSysTodo;

/**
 * 待办Service接口
 *
 * @author ruoyi
 * @date 2020-08-22
 */
public interface IDjSysTodoService
{

    public int getTodoCount(String status);
    /**
     * 查询待办
     *
     * @param todoId 待办ID
     * @return 待办
     */
    public DjSysTodo selectDjSysTodoById(Long todoId);


    /**
     * 查询待办列表
     *
     * @param djSysTodo 待办
     * @return 待办集合
     */
    public List<DjSysTodo> selectDjSysTodoList(DjSysTodo djSysTodo);

    /**
     * 新增待办
     *
     * @param djSysTodo 待办
     * @return 结果
     */
    public int insertDjSysTodo(DjSysTodo djSysTodo);

    /**
     * 修改待办
     *
     * @param djSysTodo 待办
     * @return 结果
     */
    public int updateDjSysTodo(DjSysTodo djSysTodo);


    /**
     * cancel 取消待办
     * @param uuid
     * @return
     */
    public int cancelDjSysTodoBatch(String uuid);
    /**
     * 批量删除待办
     *
     * @param todoIds 需要删除的待办ID
     * @return 结果
     */
    public int deleteDjSysTodoByIds(Long[] todoIds);

    /**
     * 删除待办信息
     *
     * @param todoId 待办ID
     * @return 结果
     */
    public int deleteDjSysTodoById(Long todoId);
}
