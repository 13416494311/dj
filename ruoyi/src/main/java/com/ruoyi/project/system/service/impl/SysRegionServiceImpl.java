package com.ruoyi.project.system.service.impl;

import com.ruoyi.project.system.domain.SysRegion;
import com.ruoyi.project.system.domain.vo.JsonTreeData;
import com.ruoyi.project.system.mapper.SysRegionMapper;
import com.ruoyi.project.system.service.ISysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 地区代码Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class SysRegionServiceImpl implements ISysRegionService
{
    @Autowired
    private SysRegionMapper sysRegionMapper;

    /**
     * 查询地区代码
     *
     * @param id 地区代码ID
     * @return 地区代码
     */
    @Override
    public SysRegion selectSysRegionById(Long id)
    {
        return sysRegionMapper.selectSysRegionById(id);
    }

    @Override
    public SysRegion selectSysRegionByRegionCode(String regionCode)
    {
        SysRegion sysRegion = new SysRegion();
        sysRegion.setRegionCode(regionCode);
        List<SysRegion> sysRegionList = sysRegionMapper.selectSysRegionList(sysRegion);
        if(sysRegionList != null && sysRegionList.size()>0){
            return sysRegionList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 查询地区代码列表
     *
     * @param sysRegion 地区代码
     * @return 地区代码
     */
    @Override
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion)
    {
        return sysRegionMapper.selectSysRegionList(sysRegion);
    }

    @Override
    public List<JsonTreeData> getRegionTree()
    {
        return sysRegionMapper.getRegionTree();
    }


}
