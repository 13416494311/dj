package com.ruoyi.project.activity.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.project.activity.domain.DjActivityArrange;

/**
 * 活动安排Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-15
 */
public interface DjActivityArrangeMapper
{
    /**
     * 查询活动安排
     *
     * @param id 活动安排ID
     * @return 活动安排
     */
    public DjActivityArrange selectDjActivityArrangeById(Long id);

    /**
     * 查询活动安排列表
     *
     * @param djActivityArrange 活动安排
     * @return 活动安排集合
     */
    public List<DjActivityArrange> selectDjActivityArrangeList(DjActivityArrange djActivityArrange);

    public List<DjActivityArrange> selectArrangeListByParamMap(Map<String,Object> map);

    /**
     * 新增活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    public int insertDjActivityArrange(DjActivityArrange djActivityArrange);

    /**
     * 修改活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    public int updateDjActivityArrange(DjActivityArrange djActivityArrange);

    /**
     * 删除活动安排
     *
     * @param id 活动安排ID
     * @return 结果
     */
    public int deleteDjActivityArrangeById(Long id);

    /**
     * 批量删除活动安排
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjActivityArrangeByIds(Long[] ids);
}
