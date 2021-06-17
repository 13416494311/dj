package com.ruoyi.project.sys.service.impl;

import java.util.List;

import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.sys.mapper.DjSysLogMapper;
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.service.IDjSysLogService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 操作日志Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-18
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjSysLogServiceImpl implements IDjSysLogService
{
    @Autowired
    private DjSysLogMapper djSysLogMapper;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询操作日志
     *
     * @param id 操作日志ID
     * @return 操作日志
     */
    @Override
    public DjSysLog selectDjSysLogById(Long id)
    {
        return djSysLogMapper.selectDjSysLogById(id);
    }

    /**
     * 查询操作日志列表
     *
     * @param djSysLog 操作日志
     * @return 操作日志
     */
    @Override
    public List<DjSysLog> selectDjSysLogList(DjSysLog djSysLog)
    {
        List<DjSysLog> sysLogList = djSysLogMapper.selectDjSysLogList(djSysLog);
        sysLogList.stream().forEach(sysLog->{
            sysLog.setSysUser(sysUserService.selectUserById(sysLog.getOperUserId()));
        });
        return sysLogList;
    }

    /**
     * 新增操作日志
     *
     * @param djSysLog 操作日志
     * @return 结果
     */
    @Override
    public int insertDjSysLog(DjSysLog djSysLog)
    {
        return djSysLogMapper.insertDjSysLog(djSysLog);
    }

    /**
     * 修改操作日志
     *
     * @param djSysLog 操作日志
     * @return 结果
     */
    @Override
    public int updateDjSysLog(DjSysLog djSysLog)
    {
        return djSysLogMapper.updateDjSysLog(djSysLog);
    }

    /**
     * 批量删除操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteDjSysLogByIds(Long[] ids)
    {
        return djSysLogMapper.deleteDjSysLogByIds(ids);
    }

    /**
     * 删除操作日志信息
     *
     * @param id 操作日志ID
     * @return 结果
     */
    @Override
    public int deleteDjSysLogById(Long id)
    {
        return djSysLogMapper.deleteDjSysLogById(id);
    }
}
