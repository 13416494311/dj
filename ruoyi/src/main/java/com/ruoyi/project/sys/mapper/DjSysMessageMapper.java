package com.ruoyi.project.sys.mapper;

import java.util.List;
import com.ruoyi.project.sys.domain.DjSysMessage;

/**
 * APP消息Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-03
 */
public interface DjSysMessageMapper 
{
    /**
     * 查询APP消息
     * 
     * @param messageId APP消息ID
     * @return APP消息
     */
    public DjSysMessage selectDjSysMessageById(Long messageId);

    /**
     * 查询APP消息列表
     * 
     * @param djSysMessage APP消息
     * @return APP消息集合
     */
    public List<DjSysMessage> selectDjSysMessageList(DjSysMessage djSysMessage);

    /**
     * 新增APP消息
     * 
     * @param djSysMessage APP消息
     * @return 结果
     */
    public int insertDjSysMessage(DjSysMessage djSysMessage);

    /**
     * 修改APP消息
     * 
     * @param djSysMessage APP消息
     * @return 结果
     */
    public int updateDjSysMessage(DjSysMessage djSysMessage);

    /**
     * 删除APP消息
     * 
     * @param messageId APP消息ID
     * @return 结果
     */
    public int deleteDjSysMessageById(Long messageId);

    /**
     * 批量删除APP消息
     * 
     * @param messageIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjSysMessageByIds(Long[] messageIds);
}
