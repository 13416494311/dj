package com.ruoyi.project.party.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjOrgAssessment;
import com.ruoyi.project.party.mapper.DjOrgAssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentyearMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;
import com.ruoyi.project.party.service.IDjOrgAssessmentyearService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 党组织考核年Service业务层处理
 *
 * @author admin
 * @date 2021-03-10
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentyearServiceImpl implements IDjOrgAssessmentyearService
{
    @Autowired
    private DjOrgAssessmentyearMapper djOrgAssessmentyearMapper;

    @Autowired
    private DjOrgAssessmentMapper assessmentMapper;

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
        int result = djOrgAssessmentyearMapper.insertDjOrgAssessmentyear(djOrgAssessmentyear);
        publishYearAssessment(djOrgAssessmentyear);
        return result;
    }

    private void publishYearAssessment(DjOrgAssessmentyear djOrgAssessmentyear){
        if("2".equals(djOrgAssessmentyear.getOrgAssessmentStatus())){
            DjOrgAssessment djOrgAssessment =new DjOrgAssessment();
            djOrgAssessment.setAssessmentyearUuid(djOrgAssessmentyear.getAssessmentyearUuid());
            List<DjOrgAssessment> assessmentList = assessmentMapper.selectDjOrgAssessmentList(djOrgAssessment);
            assessmentList.stream().forEach(assessment -> {
                assessment.setOrgAssessmentStatus("1");
                assessmentMapper.updateDjOrgAssessment(assessment);
            });
        }
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
        int result = djOrgAssessmentyearMapper.updateDjOrgAssessmentyear(djOrgAssessmentyear);
        publishYearAssessment(djOrgAssessmentyear);
        return result;
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
