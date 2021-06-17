package com.ruoyi.project.sys.service.impl;

import java.util.List;

import com.apicloud.sdk.api.Push;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.sys.mapper.DjSysTodoMapper;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 待办Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-22
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjSysTodoServiceImpl implements IDjSysTodoService
{
    @Autowired
    private DjSysTodoMapper djSysTodoMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysUserService sysUserService;

    public int getTodoCount(String status){
        DjSysTodo djSysTodo = new DjSysTodo();
        djSysTodo.setStatus(status);
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysTodo.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        }
        return djSysTodoMapper.getTodoCount(djSysTodo);
    }
    /**
     * 查询待办
     *
     * @param todoId 待办ID
     * @return 待办
     */
    @Override
    public DjSysTodo selectDjSysTodoById(Long todoId)
    {
        DjSysTodo djSysTodo = new DjSysTodo();
        djSysTodo.setTodoId(todoId);
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())){
            djSysTodo.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        }
        return djSysTodoMapper.selectDjSysTodoById(djSysTodo);
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
        List<DjSysTodo> list = djSysTodoMapper.selectDjSysTodoList(djSysTodo);
        list.stream().forEach(sysTodo->{
            if(StringUtils.isNotNull(sysTodo.getCreateBy())){
                sysTodo.setCreateUser(sysUserService.selectUserById(Long.parseLong(sysTodo.getCreateBy())));
            }
            if(sysTodo.getType()!=null){
                sysTodo.setTypeText(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
            }
            if(sysTodo.getStatus()!=null){
                sysTodo.setStatusText(dictDataService.selectDictLabel("sys_todo_status",sysTodo.getStatus()));
            }
        });
        return list;
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
        if(StringUtils.isEmpty(djSysTodo.getCreateBy())){
            djSysTodo.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        }
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
