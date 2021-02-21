package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyMemberPoliticalBirthday;

/**
 * 政治生日Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-21
 */
public interface DjPartyMemberPoliticalBirthdayMapper 
{
    /**
     * 查询政治生日
     * 
     * @param birthdayId 政治生日ID
     * @return 政治生日
     */
    public DjPartyMemberPoliticalBirthday selectDjPartyMemberPoliticalBirthdayById(Long birthdayId);

    /**
     * 查询政治生日列表
     * 
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 政治生日集合
     */
    public List<DjPartyMemberPoliticalBirthday> selectDjPartyMemberPoliticalBirthdayList(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday);

    /**
     * 新增政治生日
     * 
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 结果
     */
    public int insertDjPartyMemberPoliticalBirthday(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday);

    /**
     * 修改政治生日
     * 
     * @param djPartyMemberPoliticalBirthday 政治生日
     * @return 结果
     */
    public int updateDjPartyMemberPoliticalBirthday(DjPartyMemberPoliticalBirthday djPartyMemberPoliticalBirthday);

    /**
     * 删除政治生日
     * 
     * @param birthdayId 政治生日ID
     * @return 结果
     */
    public int deleteDjPartyMemberPoliticalBirthdayById(Long birthdayId);

    /**
     * 批量删除政治生日
     * 
     * @param birthdayIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyMemberPoliticalBirthdayByIds(Long[] birthdayIds);
}
