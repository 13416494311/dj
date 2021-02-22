package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysRegion;
import com.ruoyi.project.system.domain.vo.JsonTreeData;

import java.util.List;

/**
 * 地区代码Service接口
 *
 * @author ruoyi
 * @date 2020-06-07
 */
public interface ISysRegionService
{
    /**
     * 查询地区代码
     *
     * @param id 地区代码ID
     * @return 地区代码
     */
    public SysRegion selectSysRegionById(Long id);

    public SysRegion selectSysRegionByRegionCode(String regionCode);

    /**
     * 查询地区代码列表
     *
     * @param sysRegion 地区代码
     * @return 地区代码集合
     */
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion);


    public List<JsonTreeData> getRegionTree();


}
