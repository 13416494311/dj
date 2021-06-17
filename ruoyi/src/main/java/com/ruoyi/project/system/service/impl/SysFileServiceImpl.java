package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysFileMapper;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.service.ISysFileService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 附件Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-11
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class SysFileServiceImpl implements ISysFileService
{
    @Autowired
    private SysFileMapper sysFileMapper;

    /**
     * 查询附件
     *
     * @param id 附件ID
     * @return 附件
     */
    @Override
    public SysFile selectSysFileById(Long id)
    {
        return sysFileMapper.selectSysFileById(id);
    }

    /**
     * 查询附件列表
     *
     * @param sysFile 附件
     * @return 附件
     */
    @Override
    public List<SysFile> selectSysFileList(SysFile sysFile)
    {
        return sysFileMapper.selectSysFileList(sysFile);
    }

    /**
     * 新增附件
     *
     * @param sysFile 附件
     * @return 结果
     */
    @Override
    public int insertSysFile(SysFile sysFile)
    {
        sysFile.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        sysFile.setCreateTime(DateUtils.getNowDate());
        return sysFileMapper.insertSysFile(sysFile);
    }

    /**
     * 修改附件
     *
     * @param sysFile 附件
     * @return 结果
     */
    @Override
    public int updateSysFile(SysFile sysFile)
    {
        sysFile.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysFile.setUpdateTime(DateUtils.getNowDate());
        return sysFileMapper.updateSysFile(sysFile);
    }

    /**
     * 批量删除附件
     *
     * @param ids 需要删除的附件ID
     * @return 结果
     */
    @Override
    public int deleteSysFileByIds(Long[] ids)
    {
        return sysFileMapper.deleteSysFileByIds(ids);
    }

    /**
     * 删除附件信息
     *
     * @param id 附件ID
     * @return 结果
     */
    @Override
    public int deleteSysFileById(Long id)
    {
        return sysFileMapper.deleteSysFileById(id);
    }
}
