package com.ruoyi.project.party.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyChange;
import com.ruoyi.project.party.domain.DjPartyChangeDetail;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.mapper.DjPartyChangeDetailMapper;
import com.ruoyi.project.party.mapper.DjPartyChangeMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.IDjPartyChangeService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysLogService;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 党组织换届Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@Service
public class DjPartyChangeServiceImpl implements IDjPartyChangeService
{
    @Autowired
    private DjPartyChangeMapper djPartyChangeMapper;

    @Autowired
    private IDjPartyOrgService partyOrgService;
    @Autowired
    private IDjSysLogService djSysLogService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private DjPartyChangeDetailMapper djPartyChangeDetailMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    /**
     * 查询党组织换届
     *
     * @param changeId 党组织换届ID
     * @return 党组织换届
     */
    @Override
    public DjPartyChange selectDjPartyChangeById(Long changeId)
    {
        return djPartyChangeMapper.selectDjPartyChangeById(changeId);
    }

    /**
     * 查询党组织换届列表
     *
     * @param djPartyChange 党组织换届
     * @return 党组织换届
     */
    @Override
    public List<DjPartyChange> selectDjPartyChangeList(DjPartyChange djPartyChange)
    {
        List<DjPartyChange> partyChangeList = djPartyChangeMapper.selectDjPartyChangeList(djPartyChange);
        partyChangeList.stream().forEach(partyChange->{
            if(partyChange.getPartyOrgId()!=null){
                partyChange.setPartyOrg(partyOrgService.selectDjPartyOrgById(partyChange.getPartyOrgId()));
            }
        });
        return partyChangeList;
    }

    /**
     * 新增党组织换届
     *
     * @param djPartyChange 党组织换届
     * @return 结果
     */
    @Override
    public int insertDjPartyChange(DjPartyChange djPartyChange)
    {
        if("2".equals(djPartyChange.getStatus())){
            createSysLogAndTodo(djPartyChange,
                    Long.parseLong(djPartyChange.getParams().get("auditUserId").toString()),
                    djPartyChange.getParams().get("operReason")==null?null:djPartyChange.getParams().get("operReason").toString());
        }
        djPartyChange.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyChange.setCreateTime(DateUtils.getNowDate());
        return djPartyChangeMapper.insertDjPartyChange(djPartyChange);
    }

    /**
     * 修改党组织换届
     *
     * @param djPartyChange 党组织换届
     * @return 结果
     */
    @Override
    public int updateDjPartyChange(DjPartyChange djPartyChange)
    {
        djPartyChange.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyChange.setUpdateTime(DateUtils.getNowDate());
        if("2".equals(djPartyChange.getStatus())){
            createSysLogAndTodo(djPartyChange,
                    Long.parseLong(djPartyChange.getParams().get("auditUserId").toString()),
                    djPartyChange.getParams().get("operReason")==null?null:djPartyChange.getParams().get("operReason").toString());
        }
        if("3".equals(djPartyChange.getStatus()) || "4".equals(djPartyChange.getStatus()) ){
            auditSysLog(djPartyChange);
        }
        return djPartyChangeMapper.updateDjPartyChange(djPartyChange);
    }

    //更新操作日志  （待办前端另外调接口更新）
    private void auditSysLog(DjPartyChange partyChange){
        String state = partyChange.getParams().get("state").toString();
        String reason = partyChange.getParams().get("reason")==null?null: partyChange.getParams().get("reason").toString();
        String sysLogId = partyChange.getParams().get("sysLogId").toString();

        DjSysLog sysLog = new DjSysLog();
        sysLog.setId(Long.parseLong(sysLogId));
        switch (state){
            case "3":
                sysLog.setOperResult("通过");
                djPartyChangePass(partyChange);
                break;
            case "4":
                sysLog.setOperResult("不通过");
                break;
            default:break;
        }
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(reason);
        djSysLogService.updateDjSysLog(sysLog);
    }

    //更换党内职务
    private void djPartyChangePass(DjPartyChange partyChange){
        DjPartyChangeDetail djPartyChangeDetail = new DjPartyChangeDetail();
        djPartyChangeDetail.setChangeUuid(partyChange.getChangeUuid());
        List<DjPartyChangeDetail> changeDetailList = djPartyChangeDetailMapper.selectDjPartyChangeDetailList(djPartyChangeDetail);
        changeDetailList.stream().forEach(changeDetail->{
            if(changeDetail.getPreMemberId()!=null){
                DjPartyMember partyMember =  djPartyMemberMapper.selectDjPartyMemberById(changeDetail.getPreMemberId());
                partyMember.setPartyPositionType("");
                djPartyMemberMapper.updateDjPartyMember(partyMember);
            }
            if(changeDetail.getCurMemberId()!=null){
                DjPartyMember partyMember =  djPartyMemberMapper.selectDjPartyMemberById(changeDetail.getCurMemberId());
                partyMember.setPartyPositionType(changeDetail.getChangeType());
                djPartyMemberMapper.updateDjPartyMember(partyMember);
            }
        });

    }

    //创建待办 日志 消息
    private void createSysLogAndTodo(DjPartyChange partyChange,Long auditUserId, String operReason){
        //生成操作记录
        DjSysLog sysLog = new DjSysLog();
        sysLog.setUuid(partyChange.getChangeUuid());
        sysLog.setStepType("his");
        sysLog.setStepName("提交审批");
        sysLog.setOperUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        sysLog.setOperResult("提交党组织换届审核");
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(operReason);
        djSysLogService.insertDjSysLog(sysLog);

        DjSysLog nextSysLog = new DjSysLog();
        nextSysLog.setUuid(partyChange.getChangeUuid());
        nextSysLog.setStepType("cur");
        nextSysLog.setStepName("党组织换届审批");
        nextSysLog.setOperUserId(auditUserId);
        djSysLogService.insertDjSysLog(nextSysLog);

        DjPartyOrg partyOrg = partyOrgService.selectDjPartyOrgById(partyChange.getPartyOrgId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(partyChange.getChangeUuid());
        sysTodo.setType("7");
        sysTodo.setTitle(partyOrg.getPartyOrgFullName()+" 换届审批");
        sysTodo.setUrlName("PartyChange");
        sysTodo.setUrlPath("todo/partyChange");
        sysTodo.setUserId(auditUserId);
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("sysLogId", nextSysLog.getId().toString());
        map.put("changeUuid", partyChange.getChangeUuid());
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
     * 批量删除党组织换届
     *
     * @param changeIds 需要删除的党组织换届ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyChangeByIds(Long[] changeIds)
    {
        return djPartyChangeMapper.deleteDjPartyChangeByIds(changeIds);
    }

    /**
     * 删除党组织换届信息
     *
     * @param changeId 党组织换届ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyChangeById(Long changeId)
    {
        return djPartyChangeMapper.deleteDjPartyChangeById(changeId);
    }
}
