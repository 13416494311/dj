package com.ruoyi.project.sys.service;

import java.util.List;
import com.ruoyi.project.sys.domain.DjSysLog;

/**
 * 操作日志Service接口
 * 
 * @author ruoyi
 * @date 2020-09-18
 */
public interface IDjSysLogService 
{
    /**
     * 查询操作日志
     * 
     * @param id 操作日志ID
     * @return 操作日志
     */
    public DjSysLog selectDjSysLogById(Long id);

    /**
     * 查询操作日志列表
     * 
     * @param djSysLog 操作日志
     * @return 操作日志集合
     */
    public List<DjSysLog> selectDjSysLogList(DjSysLog djSysLog);

    /**
     * 新增操作日志
     * 
     * @param djSysLog 操作日志
     * @return 结果
     */
    public int insertDjSysLog(DjSysLog djSysLog);

    /**
     * 修改操作日志
     * 
     * @param djSysLog 操作日志
     * @return 结果
     */
    public int updateDjSysLog(DjSysLog djSysLog);

    /**
     * 批量删除操作日志
     * 
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteDjSysLogByIds(Long[] ids);

    /**
     * 删除操作日志信息
     * 
     * @param id 操作日志ID
     * @return 结果
     */
    public int deleteDjSysLogById(Long id);
}
