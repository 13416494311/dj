package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysFile;

/**
 * 附件Service接口
 * 
 * @author ruoyi
 * @date 2020-08-11
 */
public interface ISysFileService 
{
    /**
     * 查询附件
     * 
     * @param id 附件ID
     * @return 附件
     */
    public SysFile selectSysFileById(Long id);

    /**
     * 查询附件列表
     * 
     * @param sysFile 附件
     * @return 附件集合
     */
    public List<SysFile> selectSysFileList(SysFile sysFile);

    /**
     * 新增附件
     * 
     * @param sysFile 附件
     * @return 结果
     */
    public int insertSysFile(SysFile sysFile);

    /**
     * 修改附件
     * 
     * @param sysFile 附件
     * @return 结果
     */
    public int updateSysFile(SysFile sysFile);

    /**
     * 批量删除附件
     * 
     * @param ids 需要删除的附件ID
     * @return 结果
     */
    public int deleteSysFileByIds(Long[] ids);

    /**
     * 删除附件信息
     * 
     * @param id 附件ID
     * @return 结果
     */
    public int deleteSysFileById(Long id);
}
