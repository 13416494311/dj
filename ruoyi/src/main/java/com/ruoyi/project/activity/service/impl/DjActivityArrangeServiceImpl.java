package com.ruoyi.project.activity.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.domain.DjActivityParams;
import com.ruoyi.project.activity.mapper.DjActivityPlanMapper;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.activity.service.IDjActivityMemberService;
import com.ruoyi.project.activity.service.IDjActivityPlanService;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
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
import com.ruoyi.project.activity.mapper.DjActivityArrangeMapper;
import com.ruoyi.project.activity.domain.DjActivityArrange;
import com.ruoyi.project.activity.service.IDjActivityArrangeService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动安排Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-15
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjActivityArrangeServiceImpl implements IDjActivityArrangeService
{
    @Autowired
    private DjActivityArrangeMapper djActivityArrangeMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    @Autowired
    private IDjActivityMemberService djActivityMemberService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询活动安排
     *
     * @param id 活动安排ID
     * @return 活动安排
     */
    @Override
    public DjActivityArrange selectDjActivityArrangeById(Long id)
    {
        DjActivityArrange arrange = djActivityArrangeMapper.selectDjActivityArrangeById(id) ;
        if(StringUtils.isNotNull(arrange.getPartyMemberId())){
            arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
        }
        if(StringUtils.isNotNull(arrange.getPartyOrgId())){
            arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
        }
        if(StringUtils.isNotNull(arrange.getPlanUuid())){
            arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
        }
        return arrange;
    }

    /**
     * 查询活动安排列表
     *
     * @param djActivityArrange 活动安排
     * @return 活动安排
     */
    @Override
    public List<DjActivityArrange> selectDjActivityArrangeList(DjActivityArrange djActivityArrange)
    {
        List<DjActivityArrange> list = djActivityArrangeMapper.selectDjActivityArrangeList(djActivityArrange);
        list.stream().forEach(arrange->{
            if(StringUtils.isNotNull(arrange.getPartyMemberId())){
                arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(arrange.getPartyOrgId())){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(arrange.getPlanUuid())){
                arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 查询活动安排列表
     *
     * @param map 活动安排
     * @return 活动安排
     */
    @Override
    public List<DjActivityArrange> selectArrangeListByParam(DjActivityParams params)
    {
        List<DjActivityArrange> list = djActivityArrangeMapper.selectArrangeListByParam(params);
        list.stream().forEach(arrange->{
            if(StringUtils.isNotNull(arrange.getPartyMemberId())){
                arrange.setDjPartyMember(djPartyMemberService.selectDjPartyMemberById(arrange.getPartyMemberId()));
            }
            if(StringUtils.isNotNull(arrange.getPartyOrgId())){
                arrange.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(arrange.getPartyOrgId()));
            }
            if(StringUtils.isNotNull(arrange.getPlanUuid())){
                arrange.setDjActivityPlan(djActivityPlanService.selectDjActivityPlanByPlanUuid(arrange.getPlanUuid()));
            }
        });
        return list;
    }

    /**
     * 新增活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    @Override
    public int insertDjActivityArrange(DjActivityArrange djActivityArrange)
    {
        djActivityArrange.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djActivityArrange.setCreateTime(DateUtils.getNowDate());
        return djActivityArrangeMapper.insertDjActivityArrange(djActivityArrange);
    }

    /**
     * 修改活动安排
     *
     * @param djActivityArrange 活动安排
     * @return 结果
     */
    @Override
    public int updateDjActivityArrange(DjActivityArrange djActivityArrange)
    {


        if("2".equals(djActivityArrange.getStatus())){

            //活动安排 发布时 有代办就修改为已办
            DjSysTodo sysTodo = new DjSysTodo();
            sysTodo.setUuid(djActivityArrange.getId().toString());
            sysTodo.setType("6");
            List<DjSysTodo> sysTodoList = djSysTodoService.selectDjSysTodoList(sysTodo);
            sysTodoList.stream().forEach(djSysTodo->{
                djSysTodo.setStatus("1");
                djSysTodoService.updateDjSysTodo(djSysTodo);
            });

            DjActivityDetailed detailed =new DjActivityDetailed();
            detailed.setPlanUuid(djActivityArrange.getPlanUuid());
            detailed.setPartyOrgId(djActivityArrange.getPartyOrgId());
            List<DjActivityDetailed> detailedList = djActivityDetailedService.selectDjActivityDetailedList(detailed);

            DjActivityMember activityMember = new DjActivityMember();
            activityMember.setPlanUuid(djActivityArrange.getPlanUuid());
            activityMember.setPartyOrgId(djActivityArrange.getPartyOrgId());
            List<DjActivityMember> memberList = djActivityMemberService.selectDjActivityMemberList(activityMember);


            detailedList.stream().forEach(activityDetailed->{

                activityDetailed.setPartyMemberId(djActivityArrange.getPartyMemberId());
                activityDetailed.setVenue(djActivityArrange.getVenue());
                activityDetailed.setStatus("1");
                djActivityDetailedService.updateDjActivityDetailed(activityDetailed);

                createTodo(activityDetailed);

                for(int i=0;i<memberList.size();i++){
                    DjActivityMember detailedMember = new DjActivityMember();
                    detailedMember.setDetailedUuid(activityDetailed.getDetailedUuid());
                    detailedMember.setPartyMemberId(memberList.get(i).getPartyMemberId());
                    detailedMember.setType("1");
                    detailedMember.setStatus("1");
                    djActivityMemberService.insertDjActivityMember(detailedMember);
                }
            });


        }
        djActivityArrange.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivityArrange.setUpdateTime(DateUtils.getNowDate());
        return djActivityArrangeMapper.updateDjActivityArrange(djActivityArrange);
    }

    private void createTodo(DjActivityDetailed detailed){
        SysUser user = userService.selectUserByPartyMemberId(detailed.getPartyMemberId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(detailed.getDetailedUuid());
        sysTodo.setType("4"); //活动管理
        sysTodo.setTitle(detailed.getDjActivityPlan().getActivityTheme());
        sysTodo.setUrlName("ActivityDetailed");
        sysTodo.setUrlPath("todo/activityDetailed");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("detailedUuid", detailed.getDetailedUuid());
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
    /**
     * 批量删除活动安排
     *
     * @param ids 需要删除的活动安排ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityArrangeByIds(Long[] ids)
    {
        return djActivityArrangeMapper.deleteDjActivityArrangeByIds(ids);
    }

    /**
     * 删除活动安排信息
     *
     * @param id 活动安排ID
     * @return 结果
     */
    @Override
    public int deleteDjActivityArrangeById(Long id)
    {
        DjActivityArrange djActivityArrange =new DjActivityArrange();
        djActivityArrange.setId(id);
        djActivityArrange.setDelFlag("1");
        return djActivityArrangeMapper.updateDjActivityArrange(djActivityArrange);
    }
}
