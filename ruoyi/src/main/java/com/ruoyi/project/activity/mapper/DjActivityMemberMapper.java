package com.ruoyi.project.activity.mapper;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivityMember;

/**
 * 活动参与人Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-16
 */
public interface DjActivityMemberMapper 
{
    /**
     * 查询活动参与人
     * 
     * @param memberId 活动参与人ID
     * @return 活动参与人
     */
    public DjActivityMember selectDjActivityMemberById(Long memberId);

    /**
     * 查询活动参与人列表
     * 
     * @param djActivityMember 活动参与人
     * @return 活动参与人集合
     */
    public List<DjActivityMember> selectDjActivityMemberList(DjActivityMember djActivityMember);

    /**
     * 新增活动参与人
     * 
     * @param djActivityMember 活动参与人
     * @return 结果
     */
    public int insertDjActivityMember(DjActivityMember djActivityMember);

    /**
     * 修改活动参与人
     * 
     * @param djActivityMember 活动参与人
     * @return 结果
     */
    public int updateDjActivityMember(DjActivityMember djActivityMember);

    /**
     * 删除活动参与人
     * 
     * @param memberId 活动参与人ID
     * @return 结果
     */
    public int deleteDjActivityMemberById(Long memberId);

    /**
     * 批量删除活动参与人
     * 
     * @param memberIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjActivityMemberByIds(Long[] memberIds);
}
