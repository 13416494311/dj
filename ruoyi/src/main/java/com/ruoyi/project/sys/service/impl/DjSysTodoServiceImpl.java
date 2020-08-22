package com.ruoyi.project.sys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.sys.mapper.DjSysTodoMapper;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysTodoService;

/**
 * 待办Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Service
public class DjSysTodoServiceImpl implements IDjSysTodoService
{
    @Autowired
    private DjSysTodoMapper djSysTodoMapper;

    /**
     * 查询待办
     *
     * @param todoId 待办ID
     * @return 待办
     */
    @Override
    public DjSysTodo selectDjSysTodoById(Long todoId)
    {
        return djSysTodoMapper.selectDjSysTodoById(todoId);
    }

    /**
     * 查询待办列表
     *
     * @param djSysTodo 待办
     * @return 待办
     */
    @Override
    public List<DjSysTodo> selectDjSysTodoList(DjSysTodo djSysTodo)
    {
        return djSysTodoMapper.selectDjSysTodoList(djSysTodo);
    }

    /**
     * 新增待办
     *
     * @param djSysTodo 待办
     * @return 结果
     */
    @Override
    public int insertDjSysTodo(DjSysTodo djSysTodo)
    {
        djSysTodo.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djSysTodo.setCreateTime(DateUtils.getNowDate());
        return djSysTodoMapper.insertDjSysTodo(djSysTodo);
    }

    /**
     * 修改待办
     *
     * @param djSysTodo 待办
     * @return 结果
     */
    @Override
    public int updateDjSysTodo(DjSysTodo djSysTodo)
    {
        djSysTodo.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djSysTodo.setUpdateTime(DateUtils.getNowDate());
        return djSysTodoMapper.updateDjSysTodo(djSysTodo);
    }

    /**
     * cancel 取消待办
     * @param uuid
     * @return
     */
    public int cancelDjSysTodoBatch(String uuid){
        return djSysTodoMapper.cancelDjSysTodoBatch(uuid);
    }
    /**
     * 批量删除待办
     *
     * @param todoIds 需要删除的待办ID
     * @return 结果
     */
    @Override
    public int deleteDjSysTodoByIds(Long[] todoIds)
    {
        return djSysTodoMapper.deleteDjSysTodoByIds(todoIds);
    }

    /**
     * 删除待办信息
     *
     * @param todoId 待办ID
     * @return 结果
     */
    @Override
    public int deleteDjSysTodoById(Long todoId)
    {
        return djSysTodoMapper.deleteDjSysTodoById(todoId);
    }
}
