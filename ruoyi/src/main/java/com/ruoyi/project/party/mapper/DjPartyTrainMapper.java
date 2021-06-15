package com.ruoyi.project.party.mapper;

import java.util.List;
import com.ruoyi.project.party.domain.DjPartyTrain;

/**
 * 党组织培训Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-09
 */
public interface DjPartyTrainMapper
{
    /**
     * 查询党组织培训
     *
     * @param trainId 党组织培训ID
     * @return 党组织培训
     */
    public DjPartyTrain selectDjPartyTrainById(Long trainId);

    /**
     * 查询党组织培训
     *
     * @param trainUuid 党组织培训UUID
     * @return 党组织培训
     */
    public DjPartyTrain selectDjPartyTrainByUuid(String trainUuid);

    /**
     * 查询党组织培训列表
     *
     * @param djPartyTrain 党组织培训
     * @return 党组织培训集合
     */
    public List<DjPartyTrain> selectDjPartyTrainList(DjPartyTrain djPartyTrain);

    /**
     * 新增党组织培训
     *
     * @param djPartyTrain 党组织培训
     * @return 结果
     */
    public int insertDjPartyTrain(DjPartyTrain djPartyTrain);

    /**
     * 修改党组织培训
     *
     * @param djPartyTrain 党组织培训
     * @return 结果
     */
    public int updateDjPartyTrain(DjPartyTrain djPartyTrain);

    /**
     * 删除党组织培训
     *
     * @param trainId 党组织培训ID
     * @return 结果
     */
    public int deleteDjPartyTrainById(Long trainId);

    /**
     * 批量删除党组织培训
     *
     * @param trainIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDjPartyTrainByIds(Long[] trainIds);
}
