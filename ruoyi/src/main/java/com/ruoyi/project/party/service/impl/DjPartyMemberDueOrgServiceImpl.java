package com.ruoyi.project.party.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberDuePlanMapper;
import com.ruoyi.project.party.service.IDjPartyMemberDueOrgService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 党组织党费Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DjPartyMemberDueOrgServiceImpl implements IDjPartyMemberDueOrgService {
    @Autowired
    private DjPartyMemberDueOrgMapper djPartyMemberDueOrgMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private DjPartyMemberDuePlanMapper djPartyMemberDuePlanMapper;

    /**
     * 查询党组织党费
     *
     * @param dueOrgId 党组织党费ID
     * @return 党组织党费
     */
    @Override
    public DjPartyMemberDueOrg selectDjPartyMemberDueOrgById(Long dueOrgId) {

        DjPartyMemberDueOrg partyMemberDueOrg = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgById(dueOrgId);
        partyMemberDueOrg.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMemberDueOrg.getPartyOrgId()));
        partyMemberDueOrg.setDuePlan(djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanByUuid(partyMemberDueOrg.getDuePlanUuid()));
        return partyMemberDueOrg;
    }

    /**
     * 查询党组织党费列表
     *
     * @param djPartyMemberDueOrg 党组织党费
     * @return 党组织党费
     */
    @Override
    public List<DjPartyMemberDueOrg> selectDjPartyMemberDueOrgList(DjPartyMemberDueOrg djPartyMemberDueOrg) {
        List<DjPartyMemberDueOrg> partyMemberDueOrgList = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);

        partyMemberDueOrgList.stream().forEach(partyMemberDueOrg -> {
            partyMemberDueOrg.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyMemberDueOrg.getPartyOrgId()));
            partyMemberDueOrg.setDuePlan(djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanByUuid(partyMemberDueOrg.getDuePlanUuid()));
        });

        return partyMemberDueOrgList;
    }

    /**
     * 新增党组织党费
     *
     * @param djPartyMemberDueOrg 党组织党费
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberDueOrg(DjPartyMemberDueOrg djPartyMemberDueOrg) {

        djPartyMemberDueOrg.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDueOrg.setCreateTime(DateUtils.getNowDate());
        return djPartyMemberDueOrgMapper.insertDjPartyMemberDueOrg(djPartyMemberDueOrg);
    }

    /**
     * 修改党组织党费
     *
     * @param djPartyMemberDueOrg 党组织党费
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberDueOrg(DjPartyMemberDueOrg djPartyMemberDueOrg) {
        djPartyMemberDueOrg.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDueOrg.setUpdateTime(DateUtils.getNowDate());
        return djPartyMemberDueOrgMapper.updateDjPartyMemberDueOrg(djPartyMemberDueOrg);
    }

    /**
     * 批量删除党组织党费
     *
     * @param dueOrgIds 需要删除的党组织党费ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDueOrgByIds(Long[] dueOrgIds) {
        return djPartyMemberDueOrgMapper.deleteDjPartyMemberDueOrgByIds(dueOrgIds);
    }

    /**
     * 删除党组织党费信息
     *
     * @param dueOrgId 党组织党费ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDueOrgById(Long dueOrgId) {
        return djPartyMemberDueOrgMapper.deleteDjPartyMemberDueOrgById(dueOrgId);
    }
}
