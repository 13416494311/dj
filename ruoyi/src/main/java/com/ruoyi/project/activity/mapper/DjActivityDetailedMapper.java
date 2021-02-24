package com.ruoyi.project.activity.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.domain.DjActivityParams;
import org.apache.ibatis.annotations.Param;

/**
 * 活动详情Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-16
 */
public interface DjActivityDetailedMapper
{

    public List<Map<String, Object>> getActivityChartData(@Param("year") int year , @Param("status") String status);


    public int getActivityCount(DjActivityDetailed djActivityDetailed);
    /**
     * 查询活动详情
     *
     * @param activityId 活动详情ID
     * @return 活动详情
     */
    public DjActivityDetailed selectDjActivityDetailedById(Long detailedId);

    /**
     * 查询活动详情
     *
     * @param detailedUuid 活动详情UUID
     * @return 活动详情
     */
    public DjActivityDetailed selectDjActivityDetailedByDetailedUuid(String detailedUuid);

    /**
     * 查询活动详情列表
     *
     * @param djActivityDetailed 活动详情
     * @return 活动详情集合
     */
    public List<DjActivityDetailed> selectDjActivityDetailedList(DjActivityDetailed djActivityDetailed);

    /**
     * 查询活动详情列表
     *
     * @param map 活动详情
     * @return 活动详情集合
     */
    public List<DjActivityDetailed> selectDetailedListByParam(DjActivityParams params);

    /**
     * 新增活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    public int insertDjActivityDetailed(DjActivityDetailed djActivityDetailed);

    /**
     * 修改活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    public int updateDjActivityDetailed(DjActivityDetailed djActivityDetailed);

    public int updateByPlanUuidAndPartyOrgId(DjActivityDetailed djActivityDetailed);

    /**
     * 删除活动详情
     *
     * @param activityId 活动详情ID
     * @return 结果
     */
    public int deleteDjActivityDetailedById(Long activityId);

    /**
     * 批量删除活动详情
     *
     * @param activityIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjActivityDetailedByIds(Long[] activityIds);

    public List<Map<String,Object>> echarts_4();

    public Map<String,Object> count();
}
