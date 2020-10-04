package com.ruoyi.project.activity.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.domain.DjActivityParams;
import com.ruoyi.project.activity.domain.DjActivityPlan;
import com.ruoyi.project.activity.service.IDjActivityMemberService;
import com.ruoyi.project.activity.service.IDjActivityPlanService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivityDetailedMapper;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;

/**
 * 活动详情Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@Service
public class DjActivityDetailedServiceImpl implements IDjActivityDetailedService {
    @Autowired
    private DjActivityDetailedMapper djActivityDetailedMapper;
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjActivityMemberService djActivityMemberService;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;


    /**
     * 查询活动详情
     *
     * @param activityId 活动详情ID
     * @return 活动详情
     */
    @Override
    public DjActivityDetailed selectDjActivityDetailedById(Long activityId) {
        DjActivityDetailed detailed = djActivityDetailedMapper.selectDjActivityDetailedById(activityId);
        if (StringUtils.isNotNull(detailed.getPartyMemberId())) {
            detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
        }
        if (StringUtils.isNotNull(detailed.getPartyOrgId())) {
            detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
        }
        if (StringUtils.isNotNull(detailed.getPlanUuid())) {
            detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
        }
        DjActivityMember djActivityMember = new DjActivityMember();
        djActivityMember.setDetailedUuid(detailed.getDetailedUuid());
        detailed.setDjActivityMemberList(djActivityMemberService.selectDjActivityMemberList(djActivityMember));

        return detailed;
    }

    /**
     * 查询活动详情
     *
     * @param detailedUuid 活动详情UUID
     * @return 活动详情
     */
    @Override
    public DjActivityDetailed selectDjActivityDetailedByDetailedUuid(String detailedUuid) {
        DjActivityDetailed detailed = djActivityDetailedMapper.selectDjActivityDetailedByDetailedUuid(detailedUuid);
        if (StringUtils.isNotNull(detailed.getPartyMemberId())) {
            detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
        }
        if (StringUtils.isNotNull(detailed.getPartyOrgId())) {
            detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
        }
        if (StringUtils.isNotNull(detailed.getPlanUuid())) {
            detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
        }
        return detailed;
    }

    /**
     * 查询活动详情列表
     *
     * @param djActivityDetailed 活动详情
     * @return 活动详情
     */
    @Override
    public List<DjActivityDetailed> selectDjActivityDetailedList(DjActivityDetailed djActivityDetailed) {
        List<DjActivityDetailed> list = djActivityDetailedMapper.selectDjActivityDetailedList(djActivityDetailed);
        list.stream().forEach(detailed -> {
            if (StringUtils.isNotNull(detailed.getPartyMemberId())) {
                detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
            }
            if (StringUtils.isNotNull(detailed.getPartyOrgId())) {
                detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
            }
            if (StringUtils.isNotNull(detailed.getPlanUuid())) {
                detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
            }
        });
        return list;
    }

    @Override
    public List<DjActivityDetailed> selectDetailedListByParam(DjActivityParams params) {
        List<DjActivityDetailed> list = djActivityDetailedMapper.selectDetailedListByParam(params);
        list.stream().forEach(detailed -> {
            if (StringUtils.isNotNull(detailed.getPartyMemberId())) {
                detailed.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(detailed.getPartyMemberId()));
            }
            if (StringUtils.isNotNull(detailed.getPartyOrgId())) {
                detailed.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(detailed.getPartyOrgId()));
            }
            if (StringUtils.isNotNull(detailed.getPlanUuid())) {
                detailed.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 新增活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    @Override
    public int insertDjActivityDetailed(DjActivityDetailed djActivityDetailed) {
        djActivityDetailed.setDetailedUuid(UUID.randomUUID().toString());
        djActivityDetailed.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        ;
        djActivityDetailed.setCreateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.insertDjActivityDetailed(djActivityDetailed);
    }

    /**
     * 修改活动详情
     *
     * @param djActivityDetailed 活动详情
     * @return 结果
     */
    @Override
    public int updateDjActivityDetailed(DjActivityDetailed djActivityDetailed) {
        //如果更换负责人 待办跟着一起切换
        if(StringUtils.isNotNull(djActivityDetailed.getPartyMemberId())){
            DjSysTodo sysTodo = new DjSysTodo();
            sysTodo.setUuid(djActivityDetailed.getDetailedUuid());
            List<DjSysTodo> sysTodoList = djSysTodoService.selectDjSysTodoList(sysTodo);

            SysUser user = userService.selectUserByPartyMemberId(djActivityDetailed.getPartyMemberId());
            sysTodoList.stream().forEach(djSysTodo->{
                djSysTodo.setUserId(user.getUserId());
                djSysTodoService.updateDjSysTodo(djSysTodo);
            });
        }

        //归档时 修改待办为已办
        if("5".equals(djActivityDetailed.getStatus())){
            DjSysTodo sysTodo = new DjSysTodo();
            sysTodo.setUuid(djActivityDetailed.getDetailedUuid());
            sysTodo.setType("4");
            List<DjSysTodo> sysTodoList = djSysTodoService.selectDjSysTodoList(sysTodo);
            sysTodoList.stream().forEach(djSysTodo->{
                djSysTodo.setStatus("1");
                djSysTodoService.updateDjSysTodo(djSysTodo);
            });
        }

        if(StringUtils.isNotNull(djActivityDetailed.getStatus())){
            createTodo(djActivityDetailed);
        }
        djActivityDetailed.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityDetailed.setUpdateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.updateDjActivityDetailed(djActivityDetailed);
    }

    private void createTodo(DjActivityDetailed detailed) {
        DjActivityPlan activityPlan = djActivityPlanService.selectDjActivityPlanByPlanUuid(detailed.getPlanUuid());

        DjActivityMember activityMember = new DjActivityMember();
        activityMember.setPlanUuid(detailed.getPlanUuid());
        activityMember.setPartyOrgId(detailed.getPartyOrgId());
        List<DjActivityMember> activityMemberList = djActivityMemberService.selectDjActivityMemberList(activityMember);

        switch (detailed.getStatus()) {
            case "2":
                activityMemberList.stream().forEach(member -> {
                    SysUser user = userService.selectUserByPartyMemberId(member.getPartyMemberId());
                    if (StringUtils.isNotNull(user)) {
                        DjSysTodo sysTodo = new DjSysTodo();
                        sysTodo.setUuid(detailed.getDetailedUuid());
                        sysTodo.setType("1"); //建言献策
                        sysTodo.setTitle(activityPlan.getActivityTheme());
                        sysTodo.setUrlName("ActivitySuggestions");
                        sysTodo.setUrlPath("todo/activitySuggestions");
                        sysTodo.setUserId(user.getUserId());
                        sysTodo.setStatus("0");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("detailedUuid", detailed.getDetailedUuid());
                        map.put("partyMemberId", member.getPartyMemberId().toString());
                        sysTodo.setUrlParams(JSON.toJSONString(map));
                        djSysTodoService.insertDjSysTodo(sysTodo);

                        DjSysMessage sysMessage = new DjSysMessage();
                        sysMessage.setMessageUuid(sysTodo.getUuid());
                        sysMessage.setTitle(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
                        sysMessage.setContent("您收到一条"+sysTodo.getTitle()+"的待办，请及时登陆系统处理!");
                        sysMessage.setType(2);
                        sysMessage.setPlatform(0);
                        sysMessage.setGroupName("");
                        sysMessage.setStatus("0");
                        sysMessage.setUserIds(sysTodo.getUserId().toString());
                        sysMessageService.insertDjSysMessage(sysMessage);
                    }
                });
                break;
            case "3":
                break;
            case "4":
                djSysTodoService.cancelDjSysTodoBatch(detailed.getDetailedUuid());
                activityMemberList.stream().forEach(member -> {
                    SysUser user = userService.selectUserByPartyMemberId(member.getPartyMemberId());
                    if (StringUtils.isNotNull(user)) {
                        DjSysTodo sysTodo = new DjSysTodo();
                        sysTodo.setUuid(detailed.getDetailedUuid());
                        sysTodo.setType("2"); //心得体会
                        sysTodo.setTitle(activityPlan.getActivityTheme());
                        sysTodo.setUrlName("ActivityExperience");
                        sysTodo.setUrlPath("todo/activityExperience");
                        sysTodo.setUserId(user.getUserId());
                        sysTodo.setStatus("0");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("detailedUuid", detailed.getDetailedUuid());
                        map.put("partyMemberId", member.getPartyMemberId().toString());
                        sysTodo.setUrlParams(JSON.toJSONString(map));
                        djSysTodoService.insertDjSysTodo(sysTodo);

                        DjSysMessage sysMessage = new DjSysMessage();
                        sysMessage.setMessageUuid(sysTodo.getUuid());
                        sysMessage.setTitle(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
                        sysMessage.setContent("您收到一条"+sysTodo.getTitle()+"的待办，请及时登陆系统处理!");
                        sysMessage.setType(2);
                        sysMessage.setPlatform(0);
                        sysMessage.setGroupName("");
                        sysMessage.setStatus("0");
                        sysMessage.setUserIds(sysTodo.getUserId().toString());
                        sysMessageService.insertDjSysMessage(sysMessage);
                    }
                });
                break;
            case "6":
                djSysTodoService.cancelDjSysTodoBatch(detailed.getDetailedUuid());
                break;
            default:
                break;
        }
    }

    @Override
    public int updateByPlanUuidAndPartyOrgId(DjActivityDetailed djActivityDetailed) {

        djActivityDetailed.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityDetailed.setUpdateTime(DateUtils.getNowDate());
        return djActivityDetailedMapper.updateByPlanUuidAndPartyOrgId(djActivityDetailed);
    }

    /**
     * 批量删除活动详情
     *
     * @param activityIds 需要删除的活动详情ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityDetailedByIds(Long[] activityIds) {
        return djActivityDetailedMapper.deleteDjActivityDetailedByIds(activityIds);
    }

    /**
     * 删除活动详情信息
     *
     * @param activityId 活动详情ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityDetailedById(Long activityId) {
        return djActivityDetailedMapper.deleteDjActivityDetailedById(activityId);
    }
}
