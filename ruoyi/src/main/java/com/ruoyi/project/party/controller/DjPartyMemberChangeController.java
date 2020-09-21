package com.ruoyi.project.party.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberFlow;
import com.ruoyi.project.party.service.IDjPartyMemberFlowService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.service.IDjSysLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.party.domain.DjPartyMemberChange;
import com.ruoyi.project.party.service.IDjPartyMemberChangeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党员变更表Controller
 *
 * @author ruoyi
 * @date 2020-09-16
 */
@RestController
@RequestMapping("/party/partyMemberChange")
public class DjPartyMemberChangeController extends BaseController
{
    @Autowired
    private IDjPartyMemberChangeService djPartyMemberChangeService;
    @Autowired
    private IDjSysLogService djSysLogService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjPartyMemberFlowService djPartyMemberFlowService;

    /**
     * 查询党员变更表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjPartyMemberChange djPartyMemberChange)
    {
        startPage();
        List<DjPartyMemberChange> list = djPartyMemberChangeService.selectDjPartyMemberChangeList(djPartyMemberChange);
        return getDataTable(list);
    }

    /**
     * 导出党员变更表列表
     */
    @Log(title = "党员变更表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMemberChange djPartyMemberChange)
    {
        List<DjPartyMemberChange> list = djPartyMemberChangeService.selectDjPartyMemberChangeList(djPartyMemberChange);
        ExcelUtil<DjPartyMemberChange> util = new ExcelUtil<DjPartyMemberChange>(DjPartyMemberChange.class);
        return util.exportExcel(list, "partyMemberChange");
    }

    /**
     * 获取党员变更表详细信息
     */
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(djPartyMemberChangeService.selectDjPartyMemberChangeById(memberId));
    }

    @GetMapping(value = "/getByMemberUuid/{memberUuid}")
    public AjaxResult getByMemberUuid(@PathVariable("memberUuid") String memberUuid)
    {
        return AjaxResult.success(djPartyMemberChangeService.selectDjPartyMemberChangeByMemberUuid(memberUuid));
    }

    /**
     * 新增党员变更表
     */
    @Log(title = "党员变更表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjPartyMemberChange djPartyMemberChange)
    {
        return toAjax(djPartyMemberChangeService.insertDjPartyMemberChange(djPartyMemberChange));
    }

    /**
     * 修改党员变更表
     */
    @Log(title = "党员变更表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String,Object> params)
    {
        DjPartyMemberChange memberChange = JSON.parseObject(JSON.toJSONString(params), DjPartyMemberChange.class);
        String state = params.get("state").toString();
        String reason = params.get("reason")==null?null: params.get("reason").toString();
        String sysLogId = params.get("sysLogId").toString();

        memberChange.setAuditState(state);
        djPartyMemberChangeService.updateDjPartyMemberChange(memberChange);


        DjSysLog sysLog = new DjSysLog();
        sysLog.setId(Long.parseLong(sysLogId));
        switch (state){
            case "3":
                sysLog.setOperResult("通过");
                changePartyMember(memberChange);
                addMemberFlow(memberChange);
                break;
            case "4":
                sysLog.setOperResult("不通过");
                break;
            default:break;
        }
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(reason);
        djSysLogService.updateDjSysLog(sysLog);



        return AjaxResult.success();
    }

    private void addMemberFlow(DjPartyMemberChange memberChange){
        DjPartyMemberFlow memberFlow = new DjPartyMemberFlow();
        memberFlow.setFlowMemberId(memberChange.getPartyMemberId());
        switch (memberChange.getChangeType()){
            case "add":
                memberFlow.setFlowType("0");
                djPartyMemberFlowService.insertDjPartyMemberFlow(memberFlow);
                break;
            case "edit":
                break;
            case "del":
                memberFlow.setFlowType("1");
                djPartyMemberFlowService.insertDjPartyMemberFlow(memberFlow);
                break;
            default:break;
        }
    }

    private void changePartyMember(DjPartyMemberChange memberChange){
        DjPartyMember partyMember = new DjPartyMember();
        switch (memberChange.getChangeType()){
            case "add" :
                partyMember = new DjPartyMember();
                BeanUtils.copyBeanProp(partyMember,memberChange);
                partyMember.setMemberId(null);
                partyMember.setCreateBy(null);
                partyMember.setCreateTime(null);
                partyMember.setUpdateBy(null);
                partyMember.setUpdateTime(null);
                djPartyMemberService.insertDjPartyMember(partyMember);
                memberChange.setPartyMemberId(partyMember.getMemberId());
                djPartyMemberChangeService.updateDjPartyMemberChange(memberChange);
                break;
            case "edit" :
                BeanUtils.copyBeanProp(partyMember,memberChange);
                partyMember.setMemberId(memberChange.getPartyMemberId());
                partyMember.setCreateBy(null);
                partyMember.setCreateTime(null);
                partyMember.setUpdateBy(null);
                partyMember.setUpdateTime(null);
                djPartyMemberService.updateDjPartyMember(partyMember);
                break;
            case "del" :
                partyMember.setMemberId(memberChange.getPartyMemberId());
                partyMember.setDelFlag("1");
                djPartyMemberService.updateDjPartyMember(partyMember);
                break;
            default:
                break;
        }
    }
    /**
     * 删除党员变更表
     */
    @Log(title = "党员变更表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(djPartyMemberChangeService.deleteDjPartyMemberChangeByIds(memberIds));
    }
}
