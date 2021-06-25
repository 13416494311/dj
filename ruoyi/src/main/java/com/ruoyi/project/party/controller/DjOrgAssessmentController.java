package com.ruoyi.project.party.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.party.domain.*;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.service.*;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 党组织考核Controller
 *
 * @author admin
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/party/assessment")
public class DjOrgAssessmentController extends BaseController
{
    @Autowired
    private IDjOrgAssessmentService djOrgAssessmentService;
    @Autowired
    private IDjOrgAssessmentListScoreService djOrgAssessmentListScoreService;
    @Autowired
    private IDjOrgAssessmentyearService djOrgAssessmentyearService;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询党组织考核列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:list')")
    @PostMapping("/list")
    @DataScope(partyOrgAlias = "doa")
    public TableDataInfo list(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        startPage();
        List<DjOrgAssessment> list = djOrgAssessmentService.selectDjOrgAssessmentList(djOrgAssessment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('party:assessment:list')")
    @PostMapping("/listRank")
    @DataScope(partyOrgAlias = "doa")
    public TableDataInfo listRank(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        startPage();
        List<DjOrgAssessment> list = djOrgAssessmentService.selectDjOrgAssessmentListRank(djOrgAssessment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('party:assessment:list')")
    @PostMapping("/list1")
    public TableDataInfo list1(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        startPage();
        List<DjOrgAssessment> assessmentList = djOrgAssessmentService.selectDjOrgAssessmentList(djOrgAssessment);

        assessmentList.stream().forEach(assessment->{
            DjOrgAssessmentListScore djOrgAssessmentListScore = new DjOrgAssessmentListScore();
            djOrgAssessmentListScore.setAssessmentUuid(assessment.getAssessmentUuid());
            djOrgAssessmentListScore.setType("2");
            assessment.setDjOrgAssessmentListScoreList(djOrgAssessmentListScoreService.selectDjOrgAssessmentListScoreList(djOrgAssessmentListScore));
        });

        return getDataTable(assessmentList);
    }

    /**
     * 导出党组织考核列表
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:export')")
    @Log(title = "党组织考核", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjOrgAssessment djOrgAssessment)
    {
        List<DjOrgAssessment> list = djOrgAssessmentService.selectDjOrgAssessmentList(djOrgAssessment);
        ExcelUtil<DjOrgAssessment> util = new ExcelUtil<DjOrgAssessment>(DjOrgAssessment.class);
        return util.exportExcel(list, "assessment");
    }

    /**
     * 获取党组织考核详细信息
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(djOrgAssessmentService.selectDjOrgAssessmentById(id));
    }

    /**
     * 新增党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:add')")
    @Log(title = "党组织考核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjOrgAssessment djOrgAssessment)
    {
        return toAjax(djOrgAssessmentService.insertDjOrgAssessment(djOrgAssessment));
    }

    /**
     * 新增活党组织考核
     */
    @Log(title = "党组织考核", businessType = BusinessType.INSERT)
    @PostMapping("/addByOrg")
    public AjaxResult addByOrg(String assessmentyearUuid,String partyOrgIds)
    {
        djOrgAssessmentService.addByOrg(assessmentyearUuid,partyOrgIds);
        return AjaxResult.success();
    }

    /**
     * 修改党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:edit')")
    @Log(title = "党组织考核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjOrgAssessment djOrgAssessment)
    {

        return toAjax(djOrgAssessmentService.updateDjOrgAssessment(djOrgAssessment));
    }

    @PreAuthorize("@ss.hasPermi('party:assessment:edit')")
    @Log(title = "党组织考核", businessType = BusinessType.UPDATE)
    @PostMapping("/createAssessmentTodo")
    public AjaxResult createAssessmentTodo(@RequestBody DjOrgAssessment djOrgAssessment){

        if(!"2".equals(djOrgAssessment.getOrgAssessmentStatus())){
            return AjaxResult.success();
        }
        DjOrgAssessmentyear djOrgAssessmentyear =  djOrgAssessmentyearService.selectDjOrgAssessmentyearByUuid(djOrgAssessment.getAssessmentyearUuid());
        SysUser user = null;
        List<SysUser> userList = userService.selectUserByRoleId(5L);
        if( !CollectionUtils.isEmpty(userList)){
            user = userList.get(0);
        }
        if(user == null ){
            return AjaxResult.error("未查询到拥有党委角色用户。");
        }
        DjSysTodo sysTodo = new DjSysTodo();
        //sysTodo.setUuid(djPartyOrg.getPartyOrgUuid());
        sysTodo.setType("15"); //双项考评
        sysTodo.setTitle(djOrgAssessmentyear.getAssessmentName()+"("+djOrgAssessment.getDjPartyOrg().getPartyOrgName()+")");
        sysTodo.setUrlName("OrgAssessment");
        sysTodo.setUrlPath("todo/orgAssessment");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("assessmentId", String.valueOf(djOrgAssessment.getId()));
        map.put("orgAssessmentStatus", djOrgAssessment.getOrgAssessmentStatus() );
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
        return AjaxResult.success();
    }


    /**
     * 删除党组织考核
     */
    @PreAuthorize("@ss.hasPermi('party:assessment:remove')")
    @Log(title = "党组织考核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(djOrgAssessmentService.deleteDjOrgAssessmentByIds(ids));
    }
}
