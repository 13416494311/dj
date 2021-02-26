package com.ruoyi.project.members.service;

import java.util.List;
import com.ruoyi.project.members.domain.DjPartySpecialty;

/**
 * 党员特长Service接口
 * 
 * @author admin
 * @date 2021-02-19
 */
public interface IDjPartySpecialtyService 
{
    /**
     * 查询党员特长
     * 
     * @param specialtyId 党员特长ID
     * @return 党员特长
     */
    public DjPartySpecialty selectDjPartySpecialtyById(Long specialtyId);

    /**
     * 查询党员特长列表
     * 
     * @param djPartySpecialty 党员特长
     * @return 党员特长集合
     */
    public List<DjPartySpecialty> selectDjPartySpecialtyList(DjPartySpecialty djPartySpecialty);

    /**
     * 新增党员特长
     * 
     * @param djPartySpecialty 党员特长
     * @return 结果
     */
    public int insertDjPartySpecialty(DjPartySpecialty djPartySpecialty);

    /**
     * 修改党员特长
     * 
     * @param djPartySpecialty 党员特长
     * @return 结果
     */
    public int updateDjPartySpecialty(DjPartySpecialty djPartySpecialty);

    /**
     * 批量删除党员特长
     * 
     * @param specialtyIds 需要删除的党员特长ID
     * @return 结果
     */
    public int deleteDjPartySpecialtyByIds(Long[] specialtyIds);

    /**
     * 删除党员特长信息
     * 
     * @param specialtyId 党员特长ID
     * @return 结果
     */
    public int deleteDjPartySpecialtyById(Long specialtyId);
}
