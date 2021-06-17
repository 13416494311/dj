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
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.activity.mapper.DjActivitySuperviseMapper;
import com.ruoyi.project.activity.domain.DjActivitySupervise;
import com.ruoyi.project.activity.service.IDjActivitySuperviseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 活动督办Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-23
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjActivitySuperviseServiceImpl implements IDjActivitySuperviseService
{
    @Autowired
    private DjActivitySuperviseMapper djActivitySuperviseMapper;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询活动督办
     *
     * @param superviseId 活动督办ID
     * @return 活动督办
     */
    @Override
    public DjActivitySupervise selectDjActivitySuperviseById(Long superviseId)
    {
        DjActivitySupervise activitySupervise = djActivitySuperviseMapper.
                selectDjActivitySuperviseById(superviseId);
        if(StringUtils.isNotNull(activitySupervise.getPartyMemberId())){
            activitySupervise.setDjPartyMember(djPartyMemberService.
                    selectDjPartyMemberById(activitySupervise.getPartyMemberId()));
        }
        return activitySupervise;
    }

    /**
     * 查询活动督办列表
     *
     * @param djActivitySupervise 活动督办
     * @return 活动督办
     */
    @Override
    public List<DjActivitySupervise> selectDjActivitySuperviseList(DjActivitySupervise djActivitySupervise)
    {
        List<DjActivitySupervise> superviseList = djActivitySuperviseMapper.selectDjActivitySuperviseList(djActivitySupervise);
        superviseList.stream().forEach(activitySupervise->{
            if(StringUtils.isNotNull(activitySupervise.getPartyMemberId())){
                activitySupervise.setDjPartyMember(djPartyMemberService.
                        selectDjPartyMemberById(activitySupervise.getPartyMemberId()));
            }
        });
        return superviseList;
    }

    /**
     * 新增活动督办
     *
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    @Override
    public int insertDjActivitySupervise(DjActivitySupervise djActivitySupervise)
    {
        djActivitySupervise.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djActivitySupervise.setCreateTime(DateUtils.getNowDate());
        int result = djActivitySuperviseMapper.insertDjActivitySupervise(djActivitySupervise);
        createTodo(djActivitySupervise);
        return result;
    }

    private void createTodo(DjActivitySupervise djActivitySupervise){
        DjActivityDetailed detailed = djActivityDetailedService.
                selectDjActivityDetailedByDetailedUuid(djActivitySupervise.getDetailedUuid());
        SysUser user = userService.selectUserByPartyMemberId(detailed.getPartyMemberId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(detailed.getDetailedUuid());
        sysTodo.setType("5"); //活动督办
        sysTodo.setTitle(detailed.getDjActivityPlan().getActivityTheme());
        sysTodo.setUrlName("ActivityDetailed");
        sysTodo.setUrlPath("todo/activityDetailed");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("2");
        Map<String, String> map = new HashMap<String, String>();
        map.put("detailedUuid", detailed.getDetailedUuid());
        map.put("superviseId", djActivitySupervise.getSuperviseId().toString());
        sysTodo.setUrlParams(JSON.toJSONString(map));
        djSysTodoService.insertDjSysTodo(sysTodo);

        DjSysMessage sysMessage = new DjSysMessage();
        sysMessage.setMessageUuid(sysTodo.getUuid());
        sysMessage.setTitle(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
        sysMessage.setContent("您收到一条"+sysTodo.getTitle()+"的"+sysMessage.getTitle()+"，内容如下："+djActivitySupervise.getContent());
        sysMessage.setType(2);
        sysMessage.setPlatform(0);
        sysMessage.setGroupName("");
        sysMessage.setStatus("0");
        sysMessage.setUserIds(sysTodo.getUserId().toString());
        sysMessageService.insertDjSysMessage(sysMessage);
    }

    /**
     * 修改活动督办
     *
     * @param djActivitySupervise 活动督办
     * @return 结果
     */
    @Override
    public int updateDjActivitySupervise(DjActivitySupervise djActivitySupervise)
    {
        djActivitySupervise.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djActivitySupervise.setUpdateTime(DateUtils.getNowDate());
        return djActivitySuperviseMapper.updateDjActivitySupervise(djActivitySupervise);
    }

    /**
     * 批量删除活动督办
     *
     * @param superviseIds 需要删除的活动督办ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuperviseByIds(Long[] superviseIds)
    {
        return djActivitySuperviseMapper.deleteDjActivitySuperviseByIds(superviseIds);
    }

    /**
     * 删除活动督办信息
     *
     * @param superviseId 活动督办ID
     * @return 结果
     */
    @Override
    public int deleteDjActivitySuperviseById(Long superviseId)
    {
        return djActivitySuperviseMapper.deleteDjActivitySuperviseById(superviseId);
    }
}
