package com.ruoyi.project.party.controller;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberChange;
import com.ruoyi.project.party.service.IDjPartyMemberChangeService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.sys.domain.DjSysLog;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysLogService;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysConfigService;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 党员信息Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@RestController
@RequestMapping("/party/member")
public class DjPartyMemberController extends BaseController
{
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjPartyMemberChangeService djPartyMemberChangeService;
    @Autowired
    private IDjSysLogService djSysLogService;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;


    @GetMapping("/getMemberCount")
    public AjaxResult getMemberCount()
    {
        return AjaxResult.success(djPartyMemberService.getMemberCount());
    }

    /**
     * 查询党员信息列表
     */
    @RequestMapping("/list")
    public TableDataInfo list(DjPartyMember djPartyMember)
    {
        startPage();
        List<DjPartyMember> list = djPartyMemberService.selectDjPartyMemberList(djPartyMember);
        return getDataTable(list);
    }

    @PostMapping("/listForApp")
    public AjaxResult listForApp(@RequestBody DjPartyMember djPartyMember)
    {
        startPage();
        List<DjPartyMember> list = djPartyMemberService.selectPartyMemberList(djPartyMember);
        return AjaxResult.success(list);
    }


    /**
     * 导出党员信息列表
     */
    @Log(title = "党员信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyMember djPartyMember)
    {
        List<DjPartyMember> list = djPartyMemberService.selectDjPartyMemberList(djPartyMember);
        ExcelUtil<DjPartyMember> util = new ExcelUtil<DjPartyMember>(DjPartyMember.class);
        return util.exportExcel(list, "partyMember");
    }

    /**
     * 获取党员信息详细信息
     */
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(djPartyMemberService.selectDjPartyMemberById(memberId));
    }

    /**
     * 新增党员信息
     */
    @Log(title = "党员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String,Object> params)
    {
        //党员变更表
        DjPartyMemberChange memberChange = JSON.parseObject(JSON.toJSONString(params), DjPartyMemberChange.class);
        memberChange.setMemberUuid(UUID.randomUUID().toString());
        memberChange.setChangeType("add");
        memberChange.setAuditState("2");
        djPartyMemberChangeService.insertDjPartyMemberChange(memberChange);

        createSysLogAndTodo( memberChange,
                Long.parseLong(params.get("auditUserId").toString()),
                params.get("operReason")==null?null:params.get("operReason").toString());

        return AjaxResult.success(memberChange);
    }

    /**
     * 修改党员信息
     */
    @Log(title = "党员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String,Object> params)
    {

        //党员变更表
        DjPartyMemberChange memberChange = JSON.parseObject(JSON.toJSONString(params), DjPartyMemberChange.class);
        memberChange.setMemberUuid(UUID.randomUUID().toString());
        memberChange.setChangeType("edit");
        memberChange.setPartyMemberId(Long.parseLong(params.get("memberId").toString()));
        memberChange.setAuditState("2");
        djPartyMemberChangeService.insertDjPartyMemberChange(memberChange);

        createSysLogAndTodo( memberChange,
                Long.parseLong(params.get("auditUserId").toString()),
                params.get("operReason")==null?null:params.get("operReason").toString());


        return AjaxResult.success(memberChange);
    }


    private void createSysLogAndTodo(DjPartyMemberChange memberChange,Long auditUserId, String operReason){
        //生成操作记录
        DjSysLog sysLog = new DjSysLog();
        sysLog.setUuid(memberChange.getMemberUuid());
        sysLog.setStepType("his");
        sysLog.setStepName("提交审批");
        sysLog.setOperUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        switch (memberChange.getChangeType()){
            case "add" : sysLog.setOperResult("提交新增变更");  break;
            case "edit" : sysLog.setOperResult("提交修改变更");  break;
            case "del" : sysLog.setOperResult("提交删除变更");  break;
            default:break;
        }
        sysLog.setOperTime(new Date());
        sysLog.setOperReason(operReason);
        djSysLogService.insertDjSysLog(sysLog);

        DjSysLog nextSysLog = new DjSysLog();
        nextSysLog.setUuid(memberChange.getMemberUuid());
        nextSysLog.setStepType("cur");
        switch (memberChange.getChangeType()){
            case "add" : nextSysLog.setStepName("新增变更审批");  break;
            case "edit" : nextSysLog.setStepName("修改变更审批");  break;
            case "del" : nextSysLog.setStepName("删除变更审批");  break;
            default:break;
        }
        nextSysLog.setOperUserId(auditUserId);
        djSysLogService.insertDjSysLog(nextSysLog);

        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(memberChange.getMemberUuid());
        sysTodo.setType("3"); //党员变更审批
        switch (memberChange.getChangeType()){
            case "add" : sysTodo.setTitle(memberChange.getMemberName()+" 新增审批");;  break;
            case "edit" : sysTodo.setTitle(memberChange.getMemberName()+" 修改审批");;  break;
            case "del" : sysTodo.setTitle(memberChange.getMemberName()+" 删除审批");;  break;
            default:break;
        }
        sysTodo.setUrlName("MemberChangeDetail");
        sysTodo.setUrlPath("todo/memberChangeDetail");
        sysTodo.setUserId(auditUserId);
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("sysLogId", nextSysLog.getId().toString());
        map.put("memberUuid", memberChange.getMemberUuid());
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
     * 删除党员信息
     */
    @Log(title = "党员信息", businessType = BusinessType.DELETE)
	@PostMapping("/del")
    public AjaxResult remove(@RequestBody Map<String,Object> params)
    {
        DjPartyMember djPartyMember = djPartyMemberService.selectDjPartyMemberById(Long.parseLong(params.get("memberId").toString()));

        DjPartyMemberChange memberChange = new DjPartyMemberChange();
        BeanUtils.copyBeanProp(memberChange,djPartyMember);
        memberChange.setMemberUuid(UUID.randomUUID().toString());
        memberChange.setChangeType("del");
        memberChange.setPartyMemberId(djPartyMember.getMemberId());
        memberChange.setAuditState("2");

        createSysLogAndTodo( memberChange,
                Long.parseLong(params.get("auditUserId").toString()),
                params.get("operReason")==null?null:params.get("operReason").toString());

        return toAjax(djPartyMemberChangeService.insertDjPartyMemberChange(memberChange));
    }

    /**
     * 头像上传
     */
    @Log(title = "党员头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            return AjaxResult.success(avatar);
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }


    /**
     *  检查姓名 手机号 身份证 邮箱等唯一性
     * @param djPartyMember
     * @return
     * @throws IOException
     */
    @PostMapping("/checkPartyMemberUnique")
    public AjaxResult checkPartyMemberUnique(@Validated @RequestBody DjPartyMember djPartyMember) throws IOException
    {

        if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberNameUnique(djPartyMember)))
        {
            return AjaxResult.success("姓名已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberMobileUnique(djPartyMember)))
        {
            return AjaxResult.success("手机号已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberEmailUnique(djPartyMember)))
        {
            return AjaxResult.success("Email已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberIdentityCardUnique(djPartyMember)))
        {
            return AjaxResult.success("身份证号已存在");
        }
        return AjaxResult.success();
    }

    /**
     *  检查姓名 手机号 身份证 邮箱等唯一性
     * @param djPartyMember
     * @return
     * @throws IOException
     */
    @PostMapping("/checkPartyMemberUniquePc")
    public AjaxResult checkPartyMemberUniquePc(@Validated @RequestBody DjPartyMember djPartyMember) throws IOException
    {

        if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberNameUnique(djPartyMember)))
        {
            return AjaxResult.success("姓名已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberMobileUnique(djPartyMember)))
        {
            return AjaxResult.success("手机号已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberEmailUnique(djPartyMember)))
        {
            return AjaxResult.success("Email已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(djPartyMemberService.checkPartyMemberIdentityCardUnique(djPartyMember)))
        {
            return AjaxResult.success("身份证号已存在");
        }
        return AjaxResult.success();
    }

}
