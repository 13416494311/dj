package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentyearMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;
import com.ruoyi.project.party.service.IDjOrgAssessmentyearService;

/**
 * 党组织考核年Service业务层处理
 * 
 * @author admin
 * @date 2021-03-10
 */
@Service
public class DjOrgAssessmentyearServiceImpl implements IDjOrgAssessmentyearService 
{
    @Autowired
    private DjOrgAssessmentyearMapper djOrgAssessmentyearMapper;

    /**
     * 查询党组织考核年
     * 
     * @param id 党组织考核年ID
     * @return 党组织考核年
     */
    @Override
    public DjOrgAssessmentyear selectDjOrgAssessmentyearById(Long id)
    {
        return djOrgAssessmentyearMapper.selectDjOrgAssessmentyearById(id);
    }

    /**
     * 查询党组织考核年列表
     * 
     * @param djOrgAssessmentyear 党组织考核年
     * @return 党组织考核年
     */
    @Override
    public List<DjOrgAssessmentyear> selectDjOrgAssessmentyearList(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        return djOrgAssessmentyearMapper.selectDjOrgAssessmentyearList(djOrgAssessmentyear);
    }

    /**
     * 新增党组织考核年
     * 
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        djOrgAssessmentyear.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentyear.setCreateTime(DateUtils.getNowDate());
        return djOrgAssessmentyearMapper.insertDjOrgAssessmentyear(djOrgAssessmentyear);
    }

    /**
     * 修改党组织考核年
     * 
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        djOrgAssessmentyear.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentyear.setUpdateTime(DateUtils.getNowDate());
        return djOrgAssessmentyearMapper.updateDjOrgAssessmentyear(djOrgAssessmentyear);
    }

    /**
     * 批量删除党组织考核年
     * 
     * @param ids 需要删除的党组织考核年ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentyearByIds(Long[] ids)
    {
        return djOrgAssessmentyearMapper.deleteDjOrgAssessmentyearByIds(ids);
    }

    /**
     * 删除党组织考核年信息
     * 
     * @param id 党组织考核年ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentyearById(Long id)
    {
        return djOrgAssessmentyearMapper.deleteDjOrgAssessmentyearById(id);
    }
}
