package com.ruoyi.project.activity.service;

import java.util.List;
import com.ruoyi.project.activity.domain.DjActivityMember;

/**
 * 活动参与人Service接口
 *
 * @author ruoyi
 * @date 2020-08-16
 */
public interface IDjActivityMemberService
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
     * 批量删除活动参与人
     *
     * @param memberIds 需要删除的活动参与人ID
     * @return 结果
     */
    public int deleteDjActivityMemberByIds(Long[] memberIds);

    /**
     * 删除活动参与人信息
     *
     * @param memberId 活动参与人ID
     * @return 结果
     */
    public int deleteDjActivityMemberById(Long memberId);

    public String signIn(String detailedUuid);
}
