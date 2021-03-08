package com.ruoyi.project.members.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.members.domain.DjPartyExemplary;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysLogService;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.members.mapper.DjPartyMemberSuggestionsMapper;
import com.ruoyi.project.members.domain.DjPartyMemberSuggestions;
import com.ruoyi.project.members.service.IDjPartyMemberSuggestionsService;

/**
 * 党员建议Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-27
 */
@Service
public class DjPartyMemberSuggestionsServiceImpl implements IDjPartyMemberSuggestionsService
{
    @Autowired
    private DjPartyMemberSuggestionsMapper djPartyMemberSuggestionsMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IDjSysLogService djSysLogService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private SysUserMapper userMapper;


    /**
     * 查询党员建议
     *
     * @param suggestionsId 党员建议ID
     * @return 党员建议
     */
    @Override
    public DjPartyMemberSuggestions selectDjPartyMemberSuggestionsById(Long suggestionsId)
    {
        DjPartyMemberSuggestions suggestions = djPartyMemberSuggestionsMapper.selectDjPartyMemberSuggestionsById(suggestionsId);
        if(suggestions.getMemberId()!=null){
            suggestions.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getMemberId()));
        }
        if(suggestions.getPartyOrgId()!=null){
            suggestions.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(suggestions.getPartyOrgId()));
        }
        if(suggestions.getHandleMemberId()!=null){
            suggestions.setHandleMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getHandleMemberId()));
        }
        return suggestions;
    }

    /**
     * 查询党员建议列表
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 党员建议
     */
    @Override
    public List<DjPartyMemberSuggestions> selectDjPartyMemberSuggestionsList(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        List<DjPartyMemberSuggestions> suggestionsList = djPartyMemberSuggestionsMapper.selectDjPartyMemberSuggestionsList(djPartyMemberSuggestions);
        suggestionsList.stream().forEach(suggestions -> {

            if(suggestions.getMemberId()!=null){
                suggestions.setPartyMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getMemberId()));
            }
            if(suggestions.getPartyOrgId()!=null){
                suggestions.setPartyOrg(djPartyOrgService.selectDjPartyOrgById(suggestions.getPartyOrgId()));
            }
            if(suggestions.getHandleMemberId()!=null){
                suggestions.setHandleMember(djPartyMemberMapper.selectDjPartyMemberById(suggestions.getHandleMemberId()));
            }
        });

        return suggestionsList;
    }

    /**
     * 新增党员建议
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    @Override
    public int insertDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        djPartyMemberSuggestions.setCreateTime(DateUtils.getNowDate());
        djPartyMemberSuggestions.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        if("2".equals(djPartyMemberSuggestions.getStatus())){
            createSysLogAndTodo(djPartyMemberSuggestions,
                    Long.parseLong(djPartyMemberSuggestions.getParams().get("auditUserId").toString()),
                    djPartyMemberSuggestions.getParams().get("operReason")==null?null:djPartyMemberSuggestions.getParams().get("operReason").toString());
        }
        return djPartyMemberSuggestionsMapper.insertDjPartyMemberSuggestions(djPartyMemberSuggestions);
    }

    /**
     * 修改党员建议
     *
     * @param djPartyMemberSuggestions 党员建议
     * @return 结果
     */
    @Override
    public int updateDjPartyMemberSuggestions(DjPartyMemberSuggestions djPartyMemberSuggestions)
    {
        djPartyMemberSuggestions.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyMemberSuggestions.setUpdateTime(DateUtils.getNowDate());
        if("2".equals(djPartyMemberSuggestions.getStatus())){
            createSysLogAndTodo(djPartyMemberSuggestions,
                    Long.parseLong(djPartyMemberSuggestions.getParams().get("auditUserId").toString()),
                    djPartyMemberSuggestions.getParams().get("operReason")==null?null:djPartyMemberSuggestions.getParams().get("operReason").toString());
        }
        if("3".equals(djPartyMemberSuggestions.getStatus())){
            createSysLogAndTodoTransfer(djPartyMemberSuggestions,
                    Long.parseLong(djPartyMemberSuggestions.getParams().get("auditMemberId").toString()),
                    Long.parseLong(djPartyMemberSuggestions.getParams().get("sysLogId").toString()),
                    djPartyMemberSuggestions.getParams().get("operReason")==null?null:djPartyMemberSuggestions.getParams().get("operReason").toString());
        }
        if("4".equals(djPartyMemberSuggestions.getStatus())){
            auditSysLog(djPartyMemberSuggestions);
        }

        return djPartyMemberSuggestionsMapper.updateDjPartyMemberSuggestions(djPartyMemberSuggestions);
    }

    //创建待办 日志 消息
    private void createSysLogAndTodoTransfer(DjPartyMemberSuggestions memberSuggestions, Long auditMemberId, Long sysLogId, String operReason){
        String  suggestionsType= dictDataService.selectDictLabel("suggestions_type",memberSuggestions.getType());


        //生成操作记录
        DjSysLog sysLog = djSysLogService.selectDjSysLogById(sysLogId);
        sysLog.setStepType("his");
        sysLog.setOperResult("转办" );
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(operReason);
        djSysLogService.updateDjSysLog(sysLog);

        SysUser user = userMapper.selectUserByPartyMemberId(auditMemberId);
        DjSysLog nextSysLog = new DjSysLog();
        nextSysLog.setUuid(memberSuggestions.getSuggestionsUuid());
        nextSysLog.setStepType("cur");
        nextSysLog.setStepName(suggestionsType+"处理");
        nextSysLog.setOperUserId(user.getUserId());
        djSysLogService.insertDjSysLog(nextSysLog);

        DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberSuggestions.getMemberId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(memberSuggestions.getSuggestionsUuid());
        if("1".equals(memberSuggestions.getType())){
            sysTodo.setType("10");  //建议
            sysTodo.setUrlPath("todo/memberSuggestions/1");
        }
        if("2".equals(memberSuggestions.getType())){
            sysTodo.setType("11");  //心愿
            sysTodo.setUrlPath("todo/memberSuggestions/2");
        }
        sysTodo.setTitle(partyMember.getMemberName()+" "+suggestionsType+"处理");
        sysTodo.setUrlName("MemberSuggestions");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("sysLogId", nextSysLog.getId().toString());
        map.put("suggestionsUuid", memberSuggestions.getSuggestionsUuid());
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

    //更新操作日志  （待办前端另外调接口更新）
    private void auditSysLog(DjPartyMemberSuggestions djPartyMemberSuggestions){
        String state = djPartyMemberSuggestions.getParams().get("state").toString();
        String reason = djPartyMemberSuggestions.getParams().get("reason")==null?
                null: djPartyMemberSuggestions.getParams().get("reason").toString();
        String sysLogId = djPartyMemberSuggestions.getParams().get("sysLogId").toString();

        DjSysLog sysLog = new DjSysLog();
        sysLog.setId(Long.parseLong(sysLogId));
        sysLog.setStepType("his");
        sysLog.setOperResult("已处理");
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(reason);
        djSysLogService.updateDjSysLog(sysLog);
    }

    //创建待办 日志 消息
    private void createSysLogAndTodo(DjPartyMemberSuggestions memberSuggestions, Long auditUserId, String operReason){
        String  suggestionsType= dictDataService.selectDictLabel("suggestions_type",memberSuggestions.getType());
        //生成操作记录
        DjSysLog sysLog = new DjSysLog();
        sysLog.setUuid(memberSuggestions.getSuggestionsUuid());
        sysLog.setStepType("his");
        sysLog.setStepName("提交" +suggestionsType);
        sysLog.setOperUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        sysLog.setOperResult("提交" + suggestionsType );
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(operReason);
        djSysLogService.insertDjSysLog(sysLog);

        DjSysLog nextSysLog = new DjSysLog();
        nextSysLog.setUuid(memberSuggestions.getSuggestionsUuid());
        nextSysLog.setStepType("cur");
        nextSysLog.setStepName(suggestionsType+"处理");
        nextSysLog.setOperUserId(auditUserId);
        djSysLogService.insertDjSysLog(nextSysLog);

        DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(memberSuggestions.getMemberId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(memberSuggestions.getSuggestionsUuid());
        if("1".equals(memberSuggestions.getType())){
            sysTodo.setType("10");  //建议
            sysTodo.setUrlPath("todo/memberSuggestions/1");
        }
        if("2".equals(memberSuggestions.getType())){
            sysTodo.setType("11");  //心愿
            sysTodo.setUrlPath("todo/memberSuggestions/2");
        }
        sysTodo.setTitle(partyMember.getMemberName()+" "+suggestionsType+"处理");
        sysTodo.setUrlName("MemberSuggestions");
        sysTodo.setUserId(auditUserId);
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("sysLogId", nextSysLog.getId().toString());
        map.put("suggestionsUuid", memberSuggestions.getSuggestionsUuid());
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
     * 批量删除党员建议
     *
     * @param suggestionsIds 需要删除的党员建议ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberSuggestionsByIds(Long[] suggestionsIds)
    {
        return djPartyMemberSuggestionsMapper.deleteDjPartyMemberSuggestionsByIds(suggestionsIds);
    }

    /**
     * 删除党员建议信息
     *
     * @param suggestionsId 党员建议ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyMemberSuggestionsById(Long suggestionsId)
    {
        return djPartyMemberSuggestionsMapper.deleteDjPartyMemberSuggestionsById(suggestionsId);
    }
}
