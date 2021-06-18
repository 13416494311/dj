package com.ruoyi.project.party.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberDue;
import com.ruoyi.project.party.domain.DjPartyMemberDueOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberDueMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberDueOrgMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyMemberDueService;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyMemberDuePlanMapper;
import com.ruoyi.project.party.domain.DjPartyMemberDuePlan;
import com.ruoyi.project.party.service.IDjPartyMemberDuePlanService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 党费计划Service业务层处理
 *
 * @author wandengfeng
 * @date 2021-06-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyMemberDuePlanServiceImpl implements IDjPartyMemberDuePlanService
{
    @Autowired
    private DjPartyMemberDuePlanMapper djPartyMemberDuePlanMapper;
    @Autowired
    private DjPartyMemberDueOrgMapper djPartyMemberDueOrgMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyMemberDueService djPartyMemberDueService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询党费计划
     *
     * @param duePlanId 党费计划ID
     * @return 党费计划
     */
    @Override
    public DjPartyMemberDuePlan selectDjPartyMemberDuePlanById(Long duePlanId)
    {
        return djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanById(duePlanId);
    }

    /**
     * 查询党费计划列表
     *
     * @param djPartyMemberDuePlan 党费计划
     * @return 党费计划
     */
    @Override
    public List<DjPartyMemberDuePlan> selectDjPartyMemberDuePlanList(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        return djPartyMemberDuePlanMapper.selectDjPartyMemberDuePlanList(djPartyMemberDuePlan);
    }

    /**
     * 新增党费计划
     *
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        djPartyMemberDuePlan.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDuePlan.setCreateTime(DateUtils.getNowDate());
        int result = djPartyMemberDuePlanMapper.insertDjPartyMemberDuePlan(djPartyMemberDuePlan);
        addMemberDue(djPartyMemberDuePlan);
        return result;
    }

    /**
     * 修改党费计划
     *
     * @param djPartyMemberDuePlan 党费计划
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberDuePlan(DjPartyMemberDuePlan djPartyMemberDuePlan)
    {
        djPartyMemberDuePlan.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberDuePlan.setUpdateTime(DateUtils.getNowDate());
        int result = djPartyMemberDuePlanMapper.updateDjPartyMemberDuePlan(djPartyMemberDuePlan);
        addMemberDue(djPartyMemberDuePlan);
        return result;
    }

    private void addMemberDue(DjPartyMemberDuePlan djPartyMemberDuePlan){
        if("2".equals(djPartyMemberDuePlan.getStatus())){
            DjPartyMemberDueOrg djPartyMemberDueOrg = new DjPartyMemberDueOrg();
            djPartyMemberDueOrg.setDuePlanUuid(djPartyMemberDuePlan.getDuePlanUuid());
            List<DjPartyMemberDueOrg> dueOrgList = djPartyMemberDueOrgMapper.selectDjPartyMemberDueOrgList(djPartyMemberDueOrg);
            dueOrgList.stream().forEach(dueOrg->{
                DjPartyMember djPartyMember = new DjPartyMember();
                djPartyMember.setPartyOrgId(dueOrg.getPartyOrgId());
                List<DjPartyMember> djPartyMemberList =djPartyMemberMapper.selectPartyMemberList(djPartyMember);
                djPartyMemberList.stream().forEach(partyMember -> {
                    for(int i=1 ;i<4;i++){
                        DjPartyMemberDue memberDue = new DjPartyMemberDue();
                        memberDue.setDueOrgId(dueOrg.getDueOrgId());
                        memberDue.setPartyMemberId(partyMember.getMemberId());
                        memberDue.setMonth(   (djPartyMemberDuePlan.getQuarter()-1)*3+i    );
                        djPartyMemberDueService.insertDjPartyMemberDue(memberDue);
                    }
                });

                createSysLogAndTodo(djPartyMemberDuePlan,dueOrg);

            });
        }
    }

    private void createSysLogAndTodo(DjPartyMemberDuePlan djPartyMemberDuePlan,DjPartyMemberDueOrg djPartyMemberDueOrg){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setPartyOrgId(djPartyMemberDueOrg.getPartyOrgId());
        djPartyMember.setPartyPositionType("1");
        List<DjPartyMember> partyMemberList = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
        if(partyMemberList ==null || partyMemberList.size() ==0){ //没有书记 就给副书记
            djPartyMember.setPartyPositionType("2");
            partyMemberList = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
            if(partyMemberList ==null || partyMemberList.size() ==0){
                return ;
            }else{
                djPartyMember =   partyMemberList.get(0);
            }
        }else{
            djPartyMember =   partyMemberList.get(0);
        }

        SysUser user = userMapper.selectUserByPartyMemberId(djPartyMember.getMemberId());

        if(user == null ){
            return ;
        }
        DjSysTodo sysTodo = new DjSysTodo();
        //sysTodo.setUuid(djPartyOrg.getPartyOrgUuid());
        sysTodo.setType("13"); //党费填报
        sysTodo.setTitle(djPartyMemberDuePlan.getTitle());
        sysTodo.setUrlName("PartyMemberDue");
        sysTodo.setUrlPath("todo/partyMemberDue");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("dueOrgId", String.valueOf(djPartyMemberDueOrg.getDueOrgId()));
        sysTodo.setUrlParams(JSON.toJSONString(map));
        sysTodo.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djSysTodoService.insertDjSysTodo(sysTodo);

        DjSysMessage sysMessage = new DjSysMessage();
        sysMessage.setMessageUuid(sysTodo.getUuid());
        sysMessage.setTitle(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
        sysMessage.setContent("您收到一条"+sysMessage.getTitle()+"，内容如下："+sysTodo.getTitle());
        sysMessage.setType(2);
        sysMessage.setPlatform(0);
        sysMessage.setGroupName("");
        sysMessage.setStatus("0");
        sysMessage.setUserIds(sysTodo.getUserId().toString());
        sysMessage.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysMessageService.insertDjSysMessage(sysMessage);
    }

    /**
     * 批量删除党费计划
     *
     * @param duePlanIds 需要删除的党费计划ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDuePlanByIds(Long[] duePlanIds)
    {
        return djPartyMemberDuePlanMapper.deleteDjPartyMemberDuePlanByIds(duePlanIds);
    }

    /**
     * 删除党费计划信息
     *
     * @param duePlanId 党费计划ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberDuePlanById(Long duePlanId)
    {
        return djPartyMemberDuePlanMapper.deleteDjPartyMemberDuePlanById(duePlanId);
    }
}
