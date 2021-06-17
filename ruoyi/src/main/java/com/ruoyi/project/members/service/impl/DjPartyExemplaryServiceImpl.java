package com.ruoyi.project.members.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.party.domain.DjPartyChange;
import com.ruoyi.project.party.domain.DjPartyChangeDetail;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
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
import com.ruoyi.project.members.mapper.DjPartyExemplaryMapper;
import com.ruoyi.project.members.domain.DjPartyExemplary;
import com.ruoyi.project.members.service.IDjPartyExemplaryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 先锋模范Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-26
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjPartyExemplaryServiceImpl implements IDjPartyExemplaryService
{
    @Autowired
    private DjPartyExemplaryMapper djPartyExemplaryMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjSysLogService djSysLogService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询先锋模范
     *
     * @param exemplaryId 先锋模范ID
     * @return 先锋模范
     */
    @Override
    public DjPartyExemplary selectDjPartyExemplaryById(Long exemplaryId)
    {

        DjPartyExemplary partyExemplary = djPartyExemplaryMapper.selectDjPartyExemplaryById(exemplaryId);
        if(partyExemplary.getPartyOrgId()!=null){
            partyExemplary.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyExemplary.getPartyOrgId()));
        }
        if(partyExemplary.getMemberId()!=null){
            DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(partyExemplary.getMemberId());
            partyExemplary.setDjPartyMember(partyMember);
        }
        return partyExemplary;
    }

    /**
     * 查询先锋模范列表
     *
     * @param djPartyExemplary 先锋模范
     * @return 先锋模范
     */
    @Override
    public List<DjPartyExemplary> selectDjPartyExemplaryList(DjPartyExemplary djPartyExemplary)
    {
        List<DjPartyExemplary> partyExemplaryList = djPartyExemplaryMapper.selectDjPartyExemplaryList(djPartyExemplary);

        partyExemplaryList.stream().forEach( partyExemplary ->{
            if(partyExemplary.getPartyOrgId()!=null){
                partyExemplary.setDjPartyOrg(djPartyOrgService.selectDjPartyOrgById(partyExemplary.getPartyOrgId()));
            }
            if(partyExemplary.getMemberId()!=null){
                DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(partyExemplary.getMemberId());
                partyExemplary.setDjPartyMember(partyMember);
            }
        });
        return partyExemplaryList;
    }

    /**
     * 新增先锋模范
     *
     * @param djPartyExemplary 先锋模范
     * @return 结果
     */
    @Override
    public int insertDjPartyExemplary(DjPartyExemplary djPartyExemplary)
    {
        djPartyExemplary.setDelFlag("0");
        djPartyExemplary.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyExemplary.setCreateTime(DateUtils.getNowDate());
        if("2".equals(djPartyExemplary.getAuditState())){
            createSysLogAndTodo(djPartyExemplary,
                    Long.parseLong(djPartyExemplary.getParams().get("auditUserId").toString()),
                    djPartyExemplary.getParams().get("operReason")==null?null:djPartyExemplary.getParams().get("operReason").toString());
        }
        return djPartyExemplaryMapper.insertDjPartyExemplary(djPartyExemplary);
    }

    /**
     * 修改先锋模范
     *
     * @param djPartyExemplary 先锋模范
     * @return 结果
     */
    @Override
    public int updateDjPartyExemplary(DjPartyExemplary djPartyExemplary)
    {
        djPartyExemplary.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyExemplary.setUpdateTime(DateUtils.getNowDate());
        if("2".equals(djPartyExemplary.getAuditState())){
            createSysLogAndTodo(djPartyExemplary,
                    Long.parseLong(djPartyExemplary.getParams().get("auditUserId").toString()),
                    djPartyExemplary.getParams().get("operReason")==null?null:djPartyExemplary.getParams().get("operReason").toString());
        }
        if("3".equals(djPartyExemplary.getAuditState()) || "4".equals(djPartyExemplary.getAuditState()) ){
            auditSysLog(djPartyExemplary);
        }

        return djPartyExemplaryMapper.updateDjPartyExemplary(djPartyExemplary);
    }

    //创建待办 日志 消息
    private void createSysLogAndTodo(DjPartyExemplary djPartyExemplary,Long auditUserId, String operReason){
        //生成操作记录
        DjSysLog sysLog = new DjSysLog();
        sysLog.setUuid(djPartyExemplary.getExemplaryUuid());
        sysLog.setStepType("his");
        sysLog.setStepName("提交审批");
        sysLog.setOperUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        sysLog.setOperResult("提交先锋模范审核");
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(operReason);
        djSysLogService.insertDjSysLog(sysLog);

        DjSysLog nextSysLog = new DjSysLog();
        nextSysLog.setUuid(djPartyExemplary.getExemplaryUuid());
        nextSysLog.setStepType("cur");
        nextSysLog.setStepName("先锋模范审批");
        nextSysLog.setOperUserId(auditUserId);
        djSysLogService.insertDjSysLog(nextSysLog);

        DjPartyMember partyMember = djPartyMemberMapper.selectDjPartyMemberById(djPartyExemplary.getMemberId());
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(djPartyExemplary.getExemplaryUuid());
        sysTodo.setType("8");
        sysTodo.setTitle(partyMember.getMemberName()+" 先锋模范审批");
        sysTodo.setUrlName("PartyExemplary");
        sysTodo.setUrlPath("todo/partyExemplary");
        sysTodo.setUserId(auditUserId);
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("sysLogId", nextSysLog.getId().toString());
        map.put("exemplaryUuid", djPartyExemplary.getExemplaryUuid());
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
    private void auditSysLog(DjPartyExemplary djPartyExemplary){
        String state = djPartyExemplary.getParams().get("state").toString();
        String reason = djPartyExemplary.getParams().get("reason")==null?
                null: djPartyExemplary.getParams().get("reason").toString();
        String sysLogId = djPartyExemplary.getParams().get("sysLogId").toString();

        DjSysLog sysLog = new DjSysLog();
        sysLog.setId(Long.parseLong(sysLogId));
        switch (state){
            case "3":
                sysLog.setOperResult("通过");
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



    /**
     * 批量删除先锋模范
     *
     * @param exemplaryIds 需要删除的先锋模范ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyExemplaryByIds(Long[] exemplaryIds)
    {
        return djPartyExemplaryMapper.deleteDjPartyExemplaryByIds(exemplaryIds);
    }

    /**
     * 删除先锋模范信息
     *
     * @param exemplaryId 先锋模范ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyExemplaryById(Long exemplaryId)
    {
        return djPartyExemplaryMapper.deleteDjPartyExemplaryById(exemplaryId);
    }
}
