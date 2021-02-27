package com.ruoyi.project.members.service;

import java.util.List;
import com.ruoyi.project.members.domain.DjPartyMemberHelp;

/**
 * 党员帮扶Service接口
 * 
 * @author ruoyi
 * @date 2021-02-27
 */
public interface IDjPartyMemberHelpService 
{
    /**
     * 查询党员帮扶
     * 
     * @param helpId 党员帮扶ID
     * @return 党员帮扶
     */
    public DjPartyMemberHelp selectDjPartyMemberHelpById(Long helpId);

    /**
     * 查询党员帮扶列表
     * 
     * @param djPartyMemberHelp 党员帮扶
     * @return 党员帮扶集合
     */
    public List<DjPartyMemberHelp> selectDjPartyMemberHelpList(DjPartyMemberHelp djPartyMemberHelp);

    /**
     * 新增党员帮扶
     * 
     * @param djPartyMemberHelp 党员帮扶
     * @return 结果
     */
    public int insertDjPartyMemberHelp(DjPartyMemberHelp djPartyMemberHelp);

    /**
     * 修改党员帮扶
     * 
     * @param djPartyMemberHelp 党员帮扶
     * @return 结果
     */
    public int updateDjPartyMemberHelp(DjPartyMemberHelp djPartyMemberHelp);

    /**
     * 批量删除党员帮扶
     * 
     * @param helpIds 需要删除的党员帮扶ID
     * @return 结果
     */
    public int deleteDjPartyMemberHelpByIds(Long[] helpIds);

    /**
     * 删除党员帮扶信息
     * 
     * @param helpId 党员帮扶ID
     * @return 结果
     */
    public int deleteDjPartyMemberHelpById(Long helpId);
}
