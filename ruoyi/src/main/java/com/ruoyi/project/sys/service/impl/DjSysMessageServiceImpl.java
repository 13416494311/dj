package com.ruoyi.project.sys.service.impl;

import java.util.List;

import com.apicloud.sdk.api.Push;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.sys.mapper.DjSysMessageMapper;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.service.IDjSysMessageService;

/**
 * APP消息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-03
 */
@Service
public class DjSysMessageServiceImpl implements IDjSysMessageService
{
    @Autowired
    private DjSysMessageMapper djSysMessageMapper;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询APP消息
     *
     * @param messageId APP消息ID
     * @return APP消息
     */
    @Override
    public DjSysMessage selectDjSysMessageById(Long messageId)
    {
        return djSysMessageMapper.selectDjSysMessageById(messageId);
    }

    /**
     * 查询APP消息列表
     *
     * @param djSysMessage APP消息
     * @return APP消息
     */
    @Override
    public List<DjSysMessage> selectDjSysMessageList(DjSysMessage djSysMessage)
    {
        List<DjSysMessage> list = djSysMessageMapper.selectDjSysMessageList(djSysMessage);
        list.stream().forEach(sysMessage->{
            if(StringUtils.isNotNull(sysMessage.getCreateBy())){
                sysMessage.setCreateUser(sysUserService.selectUserById(Long.parseLong(sysMessage.getCreateBy())));
            }
        });
        return list;
    }

    /**
     * 新增APP消息
     *
     * @param djSysMessage APP消息
     * @return 结果
     */
    @Override
    public int insertDjSysMessage(DjSysMessage djSysMessage)
    {
        if(StringUtils.isEmpty(djSysMessage.getCreateBy())){
            djSysMessage.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        }
        djSysMessage.setCreateTime(DateUtils.getNowDate());

        Push.pushMessage(djSysMessage.getTitle(), djSysMessage.getContent(),
                djSysMessage.getType(), djSysMessage.getPlatform(), djSysMessage.getGroupName(), djSysMessage.getUserIds());

        return djSysMessageMapper.insertDjSysMessage(djSysMessage);
    }

    /**
     * 修改APP消息
     *
     * @param djSysMessage APP消息
     * @return 结果
     */
    @Override
    public int updateDjSysMessage(DjSysMessage djSysMessage)
    {
        djSysMessage.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djSysMessage.setUpdateTime(DateUtils.getNowDate());
        return djSysMessageMapper.updateDjSysMessage(djSysMessage);
    }

    /**
     * 批量删除APP消息
     *
     * @param messageIds 需要删除的APP消息ID
     * @return 结果
     */
    @Override
    public int deleteDjSysMessageByIds(Long[] messageIds)
    {
        return djSysMessageMapper.deleteDjSysMessageByIds(messageIds);
    }

    /**
     * 删除APP消息信息
     *
     * @param messageId APP消息ID
     * @return 结果
     */
    @Override
    public int deleteDjSysMessageById(Long messageId)
    {
        return djSysMessageMapper.deleteDjSysMessageById(messageId);
    }
}
